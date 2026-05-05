@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.data.motion.StepCounterManager
import com.tonyxlab.smartstep.domain.ai.AiCoach
import com.tonyxlab.smartstep.domain.connectivity.ConnectivityObserver
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.domain.repository.ActivityStatsRepository
import com.tonyxlab.smartstep.domain.repository.MetricsRepository
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.AnalyticsHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.screens.home.handling.InsightHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.ResetExitHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.StepsHandler
import com.tonyxlab.smartstep.utils.MeasurementConstants.ACTIVITY_TIMEOUT_IN_SECONDS
import com.tonyxlab.smartstep.utils.MeasurementConstants.METRIC_SAVE_INTERVAL_SECONDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.time.LocalDate

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val metricsRepository: MetricsRepository,
    private val activityStatsRepository: ActivityStatsRepository,
    private val permPrefsDataStore: PermPrefsDataStore,
    private val onboardingDataStore: OnboardingDataStore,
    private val connectivityObserver: ConnectivityObserver,
    private val aiCoach: AiCoach,
    private val stepCounterManager: StepCounterManager,
    private val stepsHandler: StepsHandler,
    private val permissionHandler: PermissionHandler,
    private val resetExitHandler: ResetExitHandler,
    private val analyticsHandler: AnalyticsHandler,
    private val insightHandler: InsightHandler,

    ) : HomeBaseViewModel() {

    private var persistMetricJob: Job? = null
    private var activityTimerJob: Job? = null

    private var hasLaunchedOnce: Boolean = false
    private var hasReceivedInitialStepReading: Boolean = false
    private var currentMetricDate: LocalDate = LocalDate.now()

    private var lastStepTimestampMillis: Long = 0L
    private var isActivityOngoing: Boolean = false

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observeStepCounter()
        observePermissionStates()
        observeMetricData()
        observeInsight()
        refreshInsight()
        observeActiveSeconds()
        //updateUiTime()
        updateState {
            analyticsHandler.populateStats(it)
        }
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {

            // Permissions
            is HomeUiEvent.ShowPermissionSheet -> updateState {
                permissionHandler.showPermissionSheet(
                        state = it,
                        type = event.type
                )
            }

            HomeUiEvent.DismissPermissionDialog -> updateState {
                permissionHandler.dismissPermissionDialog(it)
            }

            HomeUiEvent.OpenPermissionsSettings -> openPermissionsSettings()
            HomeUiEvent.Continue -> handleContinue()

            is HomeUiEvent.BackgroundAccessChanged -> {
                val wasGranted = currentState.permissionUiState.isBackgroundAccessGranted

                if (wasGranted == event.granted) return

                updateState {
                    permissionHandler.updateBackgroundAccessState(
                            state = it,
                            granted = event.granted
                    )
                }

                refreshMetricsFromCurrentSteps()

                if (event.granted) {
                    if (stepCounterManager.isSensorAvailable()) {
                        sendActionEvent(HomeActionEvent.StartStepCounterService)
                    } else {
                        sendActionEvent(
                                HomeActionEvent.ShowToastMessage(
                                        messageRes = R.string.toast_text_no_sensor
                                )
                        )
                    }
                } else {
                    sendActionEvent(HomeActionEvent.StopStepCounterService)
                }
            }

            HomeUiEvent.PhysicalActivityPermissionRequested -> physicalActivityPermissionRequested()
            HomeUiEvent.ShowBackgroundPermissionSheet -> Unit
            HomeUiEvent.AllowAccess -> Unit

            // Navigation Drawer
            HomeUiEvent.OpenNavigationDrawer -> Unit

            HomeUiEvent.FixCountIssue -> updateState {
                permissionHandler.showPermissionSheet(
                        state = it,
                        type = PermissionSheetType.BACKGROUND_ACCESS
                )
            }

            HomeUiEvent.OpenPersonalSettings -> openPersonalSettings()
            HomeUiEvent.ResetSteps -> {
                updateState { resetExitHandler.showResetDialog(it) }
                syncStepsToRepository(currentState.currentSteps)
            }

            HomeUiEvent.SaveStepGoal -> saveNewStepGoal()

            is HomeUiEvent.SelectStepGoal -> updateState {
                stepsHandler.onSelectStepGoal(
                        state = it,
                        selectedSteps = event.selectedSteps
                )
            }

            HomeUiEvent.ShowExitDialog -> updateState {
                resetExitHandler.showExitDialog(it)
            }

            // Steps Editor
            HomeUiEvent.EditSteps -> updateState { stepsHandler.showStepEditor(it) }

            HomeUiEvent.ConfirmStepEditorValues -> {
                updateState { state ->
                    val updatedState = stepsHandler.confirmStepEditor(state)
                    stepsHandler.recalculateDistanceAndCalories(updatedState)
                }

                syncStepsToRepository(currentState.currentSteps)
                scheduleDailyMetricPersist()
            }

            HomeUiEvent.DismissStepEditor -> updateState {
                stepsHandler.dismissStepEditor(
                        initialState,
                        currentState
                )
            }

            HomeUiEvent.ShowDateSelector -> updateState {
                stepsHandler.showDateSelector(currentState)
            }

            // Overview Card
            HomeUiEvent.PauseStepCounting -> {
                updateState { stepsHandler.pauseStepCounting(it) }

                if (currentState.stepEditorState.paused) {
                    activityTimerJob?.cancel()
                    isActivityOngoing = false
                }
            }

            HomeUiEvent.ViewReports -> {
                sendActionEvent(HomeActionEvent.NavigateToReports)
            }
            // Date Selector
            is HomeUiEvent.OnDaySelected -> updateState {
                stepsHandler.onDaySelected(currentState, event.value)
            }

            is HomeUiEvent.OnMonthSelected -> updateState {
                stepsHandler.onMonthSelected(currentState, event.value)
            }

            is HomeUiEvent.OnYearSelected -> updateState {
                stepsHandler.onYearSelected(currentState, event.value)
            }

            HomeUiEvent.ConfirmDateSelection -> updateState {
                stepsHandler.confirmDateSelection(currentState)
            }

            HomeUiEvent.DismissDateSelector -> updateState {
                stepsHandler.dismissDateSelector(currentState)
            }

            // Goal Picker
            HomeUiEvent.ShowStepGoalSheet -> showStepGoalPicker()
            HomeUiEvent.DismissStepGoalSheet -> updateState {
                stepsHandler.closeStepGoalSheet(it)
            }

            // Motion
            HomeUiEvent.OnMotionDetected -> {/*onStepDetected()*/
            }

            // Return from Background
            HomeUiEvent.OnReturnFromBackground -> {

                if (!hasLaunchedOnce) {
                    hasLaunchedOnce = true
                    return
                }
                refreshInsight()
            }

            // Reset Dialog
            HomeUiEvent.ConfirmResetDialog -> {
                activityTimerJob?.cancel()
                isActivityOngoing = false
                lastStepTimestampMillis = 0L

                updateState { state ->
                    val updatedState = resetExitHandler.confirmResetDialog(state)
                    val resetTimeState = stepsHandler.resetActivityTime(updatedState)
                    stepsHandler.recalculateDistanceAndCalories(resetTimeState)
                }

                launch {
                    persistCurrentDailyMetric(allowDecreases = true)
                }
            }

            HomeUiEvent.DismissResetDialog -> updateState {
                resetExitHandler.dismissResetDialog(it)
            }

            // AI Coach
            HomeUiEvent.Retry -> retryInsight()
            HomeUiEvent.GetMoreInsights -> {
                sendActionEvent(HomeActionEvent.NavigateToChat)
            }

            // Exit Dialog
            HomeUiEvent.ConfirmExitDialog -> confirmExitDialog()
            HomeUiEvent.DismissExitDialog -> updateState {
                resetExitHandler.closeExitDialog(it)
            }
        }
    }

    private fun observePermissionStates() {
        launch {
            permPrefsDataStore.physicalActivityPermissionRequested.collect { requested ->
                updateState {
                    permissionHandler.updatePhysicalActivityPermissionRequested(
                            state = it,
                            requested = requested
                    )
                }
            }
        }
    }

    private fun observeMetricData() {
        launch {
            val heightFlow = onboardingDataStore.heightInCm
            val weightFlow = onboardingDataStore.weightInKg

            combine(heightFlow, weightFlow) { height, weight ->
                height to weight
            }.collect { (height, weight) ->
                updateState { state ->
                    val updatedState = state.copy(
                            metricDataState = state.metricDataState.copy(
                                    heightInCm = height,
                                    weightInKg = weight
                            )
                    )
                    stepsHandler.recalculateDistanceAndCalories(updatedState)
                }
            }
        }
    }

    private fun observeInsight() {

        launch {
            combine(
                    aiCoach.insightState,
                    connectivityObserver.isOnline()
            ) { insightState, isOnline ->
                insightState to isOnline
            }
                    .collect { (insightState, isOnline) ->
                        updateState { uiState ->
                            val handledState = insightHandler.handleInsight(
                                    state = uiState,
                                    insightState = insightState
                            )

                            handledState.copy(
                                    insightMessageState = handledState.insightMessageState.copy(
                                            isOnline = isOnline
                                    )
                            )
                        }
                    }
        }
    }

    private fun onStepDetected() {
        if (currentState.stepEditorState.paused) return

        val now = System.currentTimeMillis()
        lastStepTimestampMillis = now

        updateState { state ->
            stepsHandler.incrementSteps(state)
            /* val previousSteps = state.currentSteps
             val incrementedState = stepsHandler.incrementSteps(state)
             val newSteps = incrementedState.currentSteps

             if (stepsHandler.shouldUpdateDistanceAndCalories(previousSteps, newSteps)) {
                 stepsHandler.recalculateDistanceAndCalories(incrementedState)
             } else {
                 incrementedState
             }*/
        }

        syncStepsToRepository(currentState.currentSteps)
        // Trigger 3 - on reaching steps goal, generate an insight
        val goal = currentState.stepGoalSheetState.selectedStepsGoal
        val steps = currentState.currentSteps

        if (steps >= goal && (steps - 1) < goal) {

            refreshInsight()
        }

        if (!isActivityOngoing) {
            isActivityOngoing = true
           // startActivityTimer()
        }
    }

    private fun startActivityTimer() {
        activityTimerJob?.cancel()

        activityTimerJob = launch {
            var lastPersistedSeconds = currentState.metricDataState.activityDurationSeconds

            while (isActive) {
                delay(1_000)

                checkForDateRollover()

                val now = System.currentTimeMillis()
                val secondsSinceLastStep = (now - lastStepTimestampMillis) / 1_000

                if (secondsSinceLastStep <= ACTIVITY_TIMEOUT_IN_SECONDS) {
                    updateState { state ->
                        val updatedState = stepsHandler.addActivitySecond(state)
                        stepsHandler.updateDisplayedTime(updatedState)
                    }

                    val currentSeconds = currentState.metricDataState.activityDurationSeconds

                    if (currentSeconds - lastPersistedSeconds >= METRIC_SAVE_INTERVAL_SECONDS) {
                        lastPersistedSeconds = currentSeconds
                        scheduleDailyMetricPersist()
                    }
                } else {
                    isActivityOngoing = false
                    persistMetricJob?.cancel()
                    persistCurrentDailyMetric()
                    cancel()
                }
            }
        }
    }

    private fun refreshMetricsFromCurrentSteps() {
        if (currentState.stepEditorState.paused) return

        updateState { state ->
            stepsHandler.recalculateDistanceAndCalories(state)
        }
    }

    private fun showStepGoalPicker() {
        updateState {
            it.copy(
                    stepGoalSheetState = currentState.stepGoalSheetState.copy(
                            pickerSheetVisible = true
                    )
            )
        }
    }

    private fun openPersonalSettings() {
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun openPermissionsSettings() {
        updateState { permissionHandler.dismissPermissionDialog(it) }
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun handleContinue() {
        updateState { permissionHandler.dismissPermissionDialog(it) }
        sendActionEvent(HomeActionEvent.RequestBatteryOptimization)
    }

    private fun physicalActivityPermissionRequested() {
        launch {
            permPrefsDataStore.setPhysicalActivityPermissionRequested(true)
        }
    }

    private fun saveNewStepGoal() {
        val selectedStepGoal = currentState.stepGoalSheetState.selectedStepsGoal
        launch {

            onboardingDataStore.setDailyStepGoal(stepGoal = selectedStepGoal)
        }
        syncGoalToRepository(goal = selectedStepGoal)
        refreshInsight(overrideGoal = selectedStepGoal)
        updateState { stepsHandler.closeStepGoalSheet(it) }
    }

    private fun refreshInsight(overrideGoal: Int? = null) {

        val progress = currentState.currentSteps.toFloat() /
                currentState.stepGoalSheetState.selectedStepsGoal.coerceAtLeast(1)
        val goal = overrideGoal ?: currentState.stepGoalSheetState.selectedStepsGoal
        launch {

            /*
                             aiCoach.refreshInsight(
                                     currentSteps = currentState.currentSteps,
                                     dailyGoal = goal,
                                     progress = progress,
                                     isOnline = currentState.insightMessageState.isOnline
                             )
            */
        }
    }

    private fun retryInsight() {
        launch {
            val isOnline = connectivityObserver.isOnline()
                    .first()
            updateState {
                it.copy(insightMessageState = it.insightMessageState.copy(isOnline = isOnline))
            }

            if (!isOnline) return@launch

            refreshInsight()
        }
    }

    private fun syncStepsToRepository(steps: Int) {
        launch {
            activityStatsRepository.updateStepCount(steps = steps)
        }
    }

    private fun syncGoalToRepository(goal: Int) {
        launch {
            activityStatsRepository.updateDailyGoal(dailyGoal = goal)
        }
    }
    private fun observeStepCounter() {
        launch {
            stepCounterManager.steps.collect { steps ->

                if (currentState.stepEditorState.paused) return@collect

                checkForDateRollover()

                val isInitialReading = hasReceivedInitialStepReading.not()
                val previousSteps = currentState.currentSteps
                val hasMoved = steps > previousSteps

                if (isInitialReading) {
                    hasReceivedInitialStepReading = true
                }

                updateState { state ->
                    val updatedState = state.copy(
                            currentSteps = steps
                    )

                    stepsHandler.recalculateDistanceAndCalories(updatedState)
                }

                syncStepsToRepository(steps)

                /**
                 * Save the first reading so today's step count can enter the database,
                 * but do NOT start the activity timer from the first reading.
                 */
                if (isInitialReading) {
                    scheduleDailyMetricPersist()
                    return@collect
                }

                if (hasMoved) {
                    onStepActivityDetected()
                    scheduleDailyMetricPersist()
                }

                val goal = currentState.stepGoalSheetState.selectedStepsGoal

                if (steps >= goal && previousSteps < goal) {
                    refreshInsight()
                }
            }
        }
    }

    private fun onStepActivityDetected() {
        val now = System.currentTimeMillis()
        lastStepTimestampMillis = now

        if (!isActivityOngoing) {
            isActivityOngoing = true
            startActivityTimer()
        }
    }

    private fun scheduleDailyMetricPersist() {
        persistMetricJob?.cancel()

        persistMetricJob = launch {
            delay(1_500)
            persistCurrentDailyMetric()
        }
    }

    private fun checkForDateRollover() {
        val today = LocalDate.now()

        if (today == currentMetricDate) return

        val previousDate = currentMetricDate

        launch {
            persistCurrentDailyMetric(date = previousDate)
        }

        currentMetricDate = today

        activityTimerJob?.cancel()
        isActivityOngoing = false
        lastStepTimestampMillis = 0L

        updateState { state ->
            val resetState = state.copy(
                    currentSteps = 0
            )
            val resetTimeState = stepsHandler.resetActivityTime(resetState)
            stepsHandler.recalculateDistanceAndCalories(resetTimeState)
        }
    }

    private suspend fun persistCurrentDailyMetric(
        date: LocalDate = LocalDate.now(),
        allowDecreases: Boolean = false
    ) {
        val state = currentState
        val savedMetric = withContext(Dispatchers.IO){
            metricsRepository.getMetricForDate(date)

        }

        val stepCount = if (allowDecreases) {
            state.currentSteps
        } else {
            maxOf(
                    savedMetric?.stepCount ?: 0,
                    state.currentSteps
            )
        }

        val activeSeconds = if (allowDecreases) {
            state.metricDataState.activityDurationSeconds
        } else {
            maxOf(
                    savedMetric?.activeSeconds ?: 0,
                    state.metricDataState.activityDurationSeconds
            )
        }

        val metric = DailyMetric(
                date = date,
                stepCount = stepCount,
                calories = state.metricDataState.calories,
                activeSeconds = activeSeconds,
                distanceKm = state.metricDataState.distance
        )

        metricsRepository.upsertDailyMetric(metric)
    }
    private fun observeActiveSeconds() {
        launch {
            metricsRepository.observeMetricForDate(LocalDate.now())
                    .collect { metric ->
                        val seconds = metric?.activeSeconds ?: return@collect
                        updateState { state ->
                            val updated = state.copy(
                                    metricDataState = state.metricDataState.copy(
                                            activityDurationSeconds = seconds
                                    )
                            )
                            stepsHandler.updateDisplayedTime(updated)
                        }
                    }
        }
    }

    private fun updateUiTime() {
        launch {
            val todayMetric = withContext(Dispatchers.IO) {
                metricsRepository.getMetricForDate(LocalDate.now())
            }

            if (todayMetric != null) {
                updateState { state ->
                    val restoredState = state.copy(
                            metricDataState = state.metricDataState.copy(
                                    activityDurationSeconds = todayMetric.activeSeconds
                            )
                    )

                    stepsHandler.updateDisplayedTime(restoredState)
                }
            }
        }
    }
    private fun confirmExitDialog() {
        sendActionEvent(HomeActionEvent.CloseApp)
    }
}