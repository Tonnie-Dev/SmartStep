@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.screens.home.handling.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.ResetExitHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.StepsHandler

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val permPrefsDataStore: PermPrefsDataStore,
    private val onboardingDataStore: OnboardingDataStore
) : HomeBaseViewModel() {

    private val stepsHandler = StepsHandler()
    private val permissionHandler = PermissionHandler()
    private val resetExitHandler = ResetExitHandler()

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
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
            is HomeUiEvent.BackgroundAccessChanged -> updateState {
                permissionHandler.updateBackgroundAccessState(
                        state = it,
                        granted = event.granted
                )
            }

            HomeUiEvent.PhysicalActivityPermissionRequested -> physicalActivityPermissionRequested()
            HomeUiEvent.ShowBackgroundPermissionSheet -> Unit
            HomeUiEvent.AllowAccess -> Unit

            // Navigation Drawer
            HomeUiEvent.OpenNavigationDrawer -> Unit
            HomeUiEvent.FixCountIssue -> showPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS)
            HomeUiEvent.OpenPersonalSettings -> openPersonalSettings()
            HomeUiEvent.ResetSteps -> updateState { resetExitHandler.showResetDialog(it) }
            HomeUiEvent.SaveStepGoal -> saveStepGoalPicker()
            is HomeUiEvent.SelectStepGoal -> updateState {
                stepsHandler.onSelectStepGoal(
                        state = it,
                        selectedSteps = event.selectedSteps
                )
            }

            HomeUiEvent.ShowExitDialog -> updateState { resetExitHandler.showExitDialog(it) }

            // Steps Editor
            HomeUiEvent.EditSteps -> updateState { stepsHandler.showStepEditor(it) }
            HomeUiEvent.ConfirmStepEditorValues -> updateState {
                stepsHandler.confirmStepEditor(
                        currentState
                )
            }
            HomeUiEvent.DismissStepEditor -> updateState {
                stepsHandler.dismissStepEditor(
                        initialState,
                        currentState
                )
            }
            HomeUiEvent.ShowDateSelector -> updateState {
                stepsHandler.showDateSelector(
                        currentState
                )
            }

            // Date Selector
            is HomeUiEvent.OnDaySelected -> updateState {
                stepsHandler.onDaySelected(
                        currentState,
                        event.value
                )
            }
            is HomeUiEvent.OnMonthSelected -> updateState {
                stepsHandler.onMonthSelected(
                        currentState,
                        event.value
                )
            }
            is HomeUiEvent.OnYearSelected -> updateState {
                stepsHandler.onYearSelected(
                        currentState,
                        event.value
                )
            }
            HomeUiEvent.ConfirmDateSelection -> updateState {
                stepsHandler.confirmDateSelection(
                        currentState
                )
            }
            HomeUiEvent.DismissDateSelector -> updateState {
                stepsHandler.dismissDateSelector(
                        currentState
                )
            }

            // Goal Picker
            HomeUiEvent.ShowStepGoalSheet -> showStepGoalPicker()
            HomeUiEvent.DismissStepGoalSheet -> updateState { stepsHandler.closeStepGoalSheet(it) }

            // Motion
            HomeUiEvent.OnMotionDetected -> updateState { stepsHandler.incrementSteps(it) }

            // Reset Dialog
            HomeUiEvent.ConfirmResetDialog -> updateState { resetExitHandler.confirmResetDialog(it) }
            HomeUiEvent.DismissResetDialog -> updateState { resetExitHandler.dismissResetDialog(it) }

            // Exit Dialog
            HomeUiEvent.ConfirmExitDialog -> confirmExitDialog()
            HomeUiEvent.DismissExitDialog -> updateState { resetExitHandler.closeExitDialog(it) }
        }
    }

    // Navigation Drawer
    private fun showStepGoalPicker() {
        updateState {
            it.copy(
                    stepGoalPickerState = currentState.stepGoalPickerState
                            .copy(pickerSheetVisible = true)
            )
        }
    }

    private fun openPersonalSettings() {
        sendActionEvent(HomeActionEvent.OpenAppSettings)
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

    private fun showPermissionSheet(type: PermissionSheetType) {
        updateState {
            it.copy(
                    permissionUiState = currentState.permissionUiState
                            .copy(
                                    permissionSheetVisible = true,
                                    permissionSheetType = type
                            )
            )
        }
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

    // Step Goal Picker
    private fun saveStepGoalPicker() {
        launch {
            val selectedSteps = currentState.stepGoalPickerState.selectedStepsGoal
            onboardingDataStore.setDailyStepGoal(stepGoal = selectedSteps)
        }
        updateState { stepsHandler.closeStepGoalSheet(it) }
    }

    fun confirmExitDialog() {
        sendActionEvent(HomeActionEvent.CloseApp)
    }
}
