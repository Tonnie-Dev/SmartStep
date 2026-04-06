@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.AnalyticsHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.screens.home.handling.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.ResetExitHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.StepsHandler
import com.tonyxlab.smartstep.utils.MeasurementConstants.ACTIVITY_TIMEOUT_IN_SECONDS
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val permPrefsDataStore: PermPrefsDataStore,
    private val onboardingDataStore: OnboardingDataStore
) : HomeBaseViewModel() {

    private val stepsHandler = StepsHandler()
    private val permissionHandler = PermissionHandler()
    private val resetExitHandler = ResetExitHandler()
    private val analyticsHandler = AnalyticsHandler()

    private var activityTimerJob: Job? = null
    private var lastStepTimestampMillis: Long = 0L
    private var isActivityOngoing: Boolean = false

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
        observeMetricData()

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
                updateState {
                    permissionHandler.updateBackgroundAccessState(
                            state = it,
                            granted = event.granted
                    )
                }

                refreshMetricsFromCurrentSteps()
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
            HomeUiEvent.ResetSteps -> updateState { resetExitHandler.showResetDialog(it) }
            HomeUiEvent.SaveStepGoal -> saveStepGoalPicker()

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

            HomeUiEvent.ConfirmStepEditorValues -> updateState { state ->
                val updatedState = stepsHandler.confirmStepEditor(state)
                stepsHandler.recalculateDistanceAndCalories(updatedState)
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

            HomeUiEvent.PauseStepCounting -> {
                updateState { stepsHandler.pauseStepCounting(it) }

                if (currentState.stepEditorState.paused) {
                    activityTimerJob?.cancel()
                    isActivityOngoing = false
                }
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
            HomeUiEvent.OnMotionDetected -> onStepDetected()

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
            }

            HomeUiEvent.DismissResetDialog -> updateState {
                resetExitHandler.dismissResetDialog(it)
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

    private fun onStepDetected() {
        if (currentState.stepEditorState.paused) return

        val now = System.currentTimeMillis()
        lastStepTimestampMillis = now

        updateState { state ->
            val previousSteps = state.currentSteps
            val incrementedState = stepsHandler.incrementSteps(state)
            val newSteps = incrementedState.currentSteps

            if (stepsHandler.shouldUpdateDistanceAndCalories(previousSteps, newSteps)) {
                stepsHandler.recalculateDistanceAndCalories(incrementedState)
            } else {
                incrementedState
            }
        }

        if (!isActivityOngoing) {
            isActivityOngoing = true
            startActivityTimer()
        }
    }

    private fun startActivityTimer() {
        activityTimerJob?.cancel()

        activityTimerJob = launch {
            while (isActive) {
                delay(1000)

                val now = System.currentTimeMillis()
                val secondsSinceLastStep = (now - lastStepTimestampMillis) / 1000

                if (secondsSinceLastStep <= ACTIVITY_TIMEOUT_IN_SECONDS) {
                    updateState { state ->
                        val withNewSecond = stepsHandler.addActivitySecond(state)
                        stepsHandler.updateDisplayedTime(withNewSecond)
                    }
                } else {
                    isActivityOngoing = false
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
        updateState { permissionHandler.closePermissionSheet(it) }
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun handleContinue() {
        updateState { permissionHandler.closePermissionSheet(it) }
        sendActionEvent(HomeActionEvent.RequestBatteryOptimization)
    }

    private fun physicalActivityPermissionRequested() {
        launch {
            permPrefsDataStore.setPhysicalActivityPermissionRequested(true)
        }
    }

    private fun saveStepGoalPicker() {
        launch {
            val selectedSteps = currentState.stepGoalSheetState.selectedStepsGoal
            onboardingDataStore.setDailyStepGoal(stepGoal = selectedSteps)
        }

        updateState { stepsHandler.closeStepGoalSheet(it) }
    }

    private fun confirmExitDialog() {
        sendActionEvent(HomeActionEvent.CloseApp)
    }
}