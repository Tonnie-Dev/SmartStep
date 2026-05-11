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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
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

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
        observeHeightAndWeight()
        observeInsight()
        refreshInsight()
        observeTodayMetrics()
        observeWeeklyMetric()
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

            }

            HomeUiEvent.SaveStepGoal -> saveNewStepGoal()

            is HomeUiEvent.SelectStepGoal -> updateState {
                stepsHandler.selectStepGoal(
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

                saveEditedSteps()
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
            HomeUiEvent.ShowStepGoalSheet -> {
                updateState { state -> stepsHandler.showStepGoalPicker(state) }
            }

            HomeUiEvent.DismissStepGoalSheet -> updateState {
                stepsHandler.closeStepGoalSheet(it)
            }

            // Motion
            HomeUiEvent.OnMotionDetected -> {/*onStepDetected()*/ }

            // Return from Background
            HomeUiEvent.OnReturnFromBackground -> { refreshInsight() }

            // Reset Dialog
            HomeUiEvent.ConfirmResetDialog -> {

                updateState { state ->
                    val updatedState = resetExitHandler.confirmResetDialog(state)
                    val resetTimeState = stepsHandler.resetActivityTime(updatedState)
                    stepsHandler.recalculateDistanceAndCalories(resetTimeState)
                }

                launch {
                    resetTodaySteps()
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

    private fun observeTodayMetrics() {
        if (currentState.stepEditorState.paused) return
        launch {
            metricsRepository.observeMetricForDate(LocalDate.now())
                    .collect { metric ->

                        if (metric == null) return@collect
                        if (currentState.stepEditorState.paused) return@collect
                        updateState { state ->
                            val updatedState = state.copy(
                                    currentSteps = metric.stepCount,
                                    metricDataState = state.metricDataState.copy(
                                            activityDurationSeconds = metric.activeSeconds,
                                            calories = metric.calories,
                                            distance = metric.distanceKm
                                    )
                            )
                            stepsHandler.updateDisplayedTime(updatedState)
                        }
                    }
        }
    }

    private fun observeWeeklyMetric() {
        val today = LocalDate.now()

        launch {

            metricsRepository.getWeeklyMetrics(startDate = today.minusDays(6))
                    .collect { weeklyMetrics ->

                        updateState { uiState ->

                            analyticsHandler.populateStats(
                                    state = uiState,
                                    weeklyMetrics = weeklyMetrics
                            )
                        }
                    }

        }

    }

    private fun observeHeightAndWeight() {
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

    private fun refreshMetricsFromCurrentSteps() {
        if (currentState.stepEditorState.paused) return

        updateState { state ->
            stepsHandler.recalculateDistanceAndCalories(state)
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

    private fun refreshInsight(overrideGoal: Int? = null) {

        val progress = currentState.currentSteps.toFloat() /
                currentState.stepGoalSheetState.selectedStepsGoal.coerceAtLeast(1)
        val goal = overrideGoal ?: currentState.stepGoalSheetState.selectedStepsGoal
        /*   launch {
                                aiCoach.refreshInsight(
                                        currentSteps = currentState.currentSteps,
                                        dailyGoal = goal,
                                        progress = progress,
                                        isOnline = currentState.insightMessageState.isOnline
                                )
           }*/
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

    private fun syncGoalToRepository(goal: Int) {
        launch {
            activityStatsRepository.updateDailyGoal(dailyGoal = goal)
        }
    }

    private fun saveNewStepGoal() {
        val selectedStepGoal = currentState.stepGoalSheetState.selectedStepsGoal
        launch {
            onboardingDataStore.setDailyStepGoal(stepGoal = selectedStepGoal)
            persistTodayGoal(dailyStepGoal = selectedStepGoal)
        }
        syncGoalToRepository(goal = selectedStepGoal)
        refreshInsight(overrideGoal = selectedStepGoal)
        updateState { stepsHandler.closeStepGoalSheet(it) }
    }

    private fun saveEditedSteps() {
        launch {

            val selectedDate = currentState.stepEditorState.selectedDate
            val isToday = selectedDate == LocalDate.now()

            val steps = currentState.stepEditorState.stepsTextFieldState.text
                    .toString()
                    .trim()
                    .toIntOrNull()
                ?: 0

            // Update live sensor baseline + save to DB,  old date → save to DB only
            if (isToday) {
                stepCounterManager.editSteps(
                        steps = steps,
                        date = selectedDate
                )

                updateState { state ->
                    val updatedState = state.copy(
                            currentSteps = steps
                    )

                    stepsHandler.recalculateDistanceAndCalories(updatedState)
                }
            }

            updateState { state ->
                val updatedState = state.copy(
                        currentSteps = if (selectedDate == LocalDate.now()) {
                            steps
                        } else {
                            state.currentSteps
                        }
                )
                stepsHandler.recalculateDistanceAndCalories(updatedState)
            }

            persistManualSteps(
                    date = selectedDate,
                    steps = steps,
                    allowDecreases = true
            )
        }
    }

    private suspend fun resetTodaySteps() {
        stepCounterManager.resetSteps()
        updateState { state ->
            val resetState = state.copy(
                    currentSteps = 0,
                    metricDataState = state.metricDataState.copy(
                            activityDurationSeconds = 0,
                            distance = 0.0,
                            calories = 0
                    )
            )
            stepsHandler.updateDisplayedTime(resetState)
        }

        persistManualSteps(
                date = LocalDate.now(),
                steps = 0,
                allowDecreases = true
        )
    }

    private suspend fun persistManualSteps(
        steps: Int,
        date: LocalDate = LocalDate.now(),
        allowDecreases: Boolean = true
    ) {
        withContext(Dispatchers.IO) {
            val savedMetric = metricsRepository.getMetricForDate(date)

            val goalToSave = if (date == LocalDate.now()) {
                currentState.stepGoalSheetState.selectedStepsGoal
            } else {
                savedMetric?.dailyStepGoal ?: currentState.stepGoalSheetState.selectedStepsGoal
            }

            val metricToSave = savedMetric?.copy(
                    stepCount = steps,
                    dailyStepGoal = goalToSave,
                    activeSeconds = if (steps == 0) 0 else savedMetric.activeSeconds,
                    calories = if (steps == 0) 0 else currentState.metricDataState.calories,
                    distanceKm = if (steps == 0) 0.0 else currentState.metricDataState.distance
            ) ?: DailyMetric(
                    date = date,
                    stepCount = steps,
                    dailyStepGoal = goalToSave,
                    activeSeconds = 0,
                    calories = if (steps == 0) 0 else currentState.metricDataState.calories,
                    distanceKm = if (steps == 0) 0.0 else currentState.metricDataState.distance
            )
            metricsRepository.upsertDailyMetric(
                    newDailyMetric = metricToSave,
                    allowDecreases = allowDecreases
            )
        }
    }

    private suspend fun persistTodayGoal(
        dailyStepGoal: Int
    ) {
        withContext(Dispatchers.IO) {
            val today = LocalDate.now()
            val savedMetric = metricsRepository.getMetricForDate(today)

            val metricToSave = savedMetric?.copy(
                    dailyStepGoal = dailyStepGoal
            ) ?: DailyMetric(
                    date = today,
                    stepCount = currentState.currentSteps,
                    dailyStepGoal = dailyStepGoal,
                    activeSeconds = currentState.metricDataState.activityDurationSeconds,
                    calories = currentState.metricDataState.calories,
                    distanceKm = currentState.metricDataState.distance
            )

            metricsRepository.upsertDailyMetric(
                    newDailyMetric = metricToSave,
                    allowDecreases = true
            )
        }
    }

    private fun confirmExitDialog() {
        sendActionEvent(HomeActionEvent.CloseApp)
    }
}