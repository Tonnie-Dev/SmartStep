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
import java.time.LocalDate

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val permPrefsDataStore: PermPrefsDataStore,
    private val onboardingDataStore: OnboardingDataStore
) : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
    }

    override fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ShowPermissionSheet -> showPermissionSheet(event.type)
            HomeUiEvent.DismissPermissionDialog -> dismissPermissionDialog()
            HomeUiEvent.OpenPermissionsSettings -> openPermissionsSettings()
            HomeUiEvent.Continue -> handleContinue()
            HomeUiEvent.PhysicalActivityPermissionRequested -> physicalActivityPermissionRequested()
            HomeUiEvent.ShowExitDialog -> showExitDialog()

            // Navigation Drawer
            HomeUiEvent.OpenNavigationDrawer -> Unit
            HomeUiEvent.FixCountIssue -> showPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS)
            HomeUiEvent.OpenPersonalSettings -> openPersonalSettings()
            HomeUiEvent.ShowStepGoalPicker -> showStepGoalPicker()
            HomeUiEvent.ResetSteps -> TODO()

            is HomeUiEvent.BackgroundAccessChanged -> updateBackgroundAccessState(event.granted)
            HomeUiEvent.DismissStepGoalPicker -> dismissStepGoalPicker()
            HomeUiEvent.SaveStepGoal -> saveStepGoalPicker()
            is HomeUiEvent.SelectStepGoal -> onSelectStepGoal(event.selectedSteps)

            HomeUiEvent.ShowBackgroundPermissionSheet -> Unit
            HomeUiEvent.AllowAccess -> Unit

            // Motion
            HomeUiEvent.OnMotionDetected -> incrementSteps()

            // Steps Editor
            HomeUiEvent.EditSteps -> showEditStepDialog()
            HomeUiEvent.ConfirmStepEditorValues -> onConfirmStepEditorDialog()
            HomeUiEvent.DismissStepEditor -> dismissStepEditorDialog()
            HomeUiEvent.ShowDateSelector -> showDateSelectorDialog()
            is HomeUiEvent.OnEditSteps -> TODO()

            // Date Selector
            is HomeUiEvent.OnDaySelected -> onDaySelected(event.value)
            is HomeUiEvent.OnMonthSelected -> onMonthSelected(event.value)
            is HomeUiEvent.OnYearSelected -> onYearSelected(event.value)
            HomeUiEvent.ConfirmDateSelection -> confirmDateSelectorDialog()
            HomeUiEvent.DismissDateSelector -> dismissDateSelectorDialog()

            // Exit Dialog
            HomeUiEvent.ConfirmExitDialog -> confirmExitDialog()
            HomeUiEvent.DismissExitDialog -> dismissExitDialog()

        }
    }

    private fun incrementSteps() {
        updateState {
            it.copy(currentSteps = it.currentSteps + 1)
        }
    }

    private fun observePermissionStates() {
        launch {
            permPrefsDataStore.physicalActivityPermissionRequested.collect { requested ->
                updateState {
                    it.copy(
                            permissionUiState = currentState.permissionUiState.copy(
                                    physicalActivityPermissionRequested = requested
                            )
                    )
                }
            }
        }
    }

    private fun openPersonalSettings() {
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun onSelectStepGoal(selectedSteps: Int) {
        updateState {
            it.copy(
                    stepGoalPickerState = currentState.stepGoalPickerState
                            .copy(selectedStepsGoal = selectedSteps)
            )
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

    private fun dismissPermissionDialog() {
        updateState {
            it.copy(
                    permissionUiState = currentState.permissionUiState
                            .copy(
                                    permissionSheetVisible = false,
                                    permissionSheetType = null
                            )
            )
        }
    }

    private fun openPermissionsSettings() {
        updateState {
            it.copy(
                    permissionUiState = currentState.permissionUiState
                            .copy(permissionSheetVisible = false)
            )
        }
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun handleContinue() {
        updateState {
            it.copy(
                    permissionUiState = currentState.permissionUiState
                            .copy(permissionSheetVisible = false)
            )
        }
        sendActionEvent(HomeActionEvent.RequestBatteryOptimization)
    }

    private fun physicalActivityPermissionRequested() {
        launch {
            permPrefsDataStore.setPhysicalActivityPermissionRequested(true)
        }
    }

    private fun updateBackgroundAccessState(granted: Boolean) {

        updateState {
            it.copy(
                    permissionUiState = currentState.permissionUiState
                            .copy(isBackgroundAccessGranted = granted)
            )
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

    private fun showEditStepDialog() {
        updateState {


            it.copy(stepEditorState = currentState.stepEditorState.copy(isStepEditorVisible = true))
        }
    }

    // Step Goal Picker
    private fun saveStepGoalPicker() {
        launch {
            val selectedSteps = currentState.stepGoalPickerState.selectedStepsGoal
            onboardingDataStore.setDailyStepGoal(stepGoal = selectedSteps)

            updateState {
                it.copy(
                        stepGoalPickerState = currentState.stepGoalPickerState
                                .copy(pickerSheetVisible = false)
                )
            }
        }
    }

    private fun dismissStepGoalPicker() {
        updateState {
            it.copy(
                    stepGoalPickerState = currentState.stepGoalPickerState
                            .copy(pickerSheetVisible = false)
            )
        }
    }

    // Step Editor
    private fun onConfirmStepEditorDialog() {

        val stepsText = currentState.stepEditorState.stepsTextFieldState.text
                .toString()
                .trim()

        val steps = stepsText.toIntOrNull() ?: 0

        updateState {
            it.copy(
                    currentSteps = steps,
                    stepEditorState = currentState.stepEditorState.copy(
                            isStepEditorVisible = false
                    )
            )
        }
    }

    private fun dismissStepEditorDialog() {
        updateState {
            it.copy(
                    stepEditorState = initialState.stepEditorState,
                    dateSelectorState = initialState.dateSelectorState
            )
        }
    }

    private fun showDateSelectorDialog() {
        updateState {
            it.copy(
                    dateSelectorState = currentState.dateSelectorState.copy(
                            isDateSelectorVisible = true
                    )
            )
        }
    }

    // Date Selector
    private fun onDaySelected(day: Int) {
        updateState {
            it.copy(
                    dateSelectorState = currentState.dateSelectorState.copy(day = day)
            )
        }
    }

    private fun onMonthSelected(month: Int) {
        updateState {
            it.copy(
                    dateSelectorState = currentState.dateSelectorState.copy(month = month)
            )
        }
    }

    private fun onYearSelected(year: Int) {
        updateState {
            it.copy(
                    dateSelectorState = currentState.dateSelectorState.copy(year = year)
            )
        }
    }

    private fun confirmDateSelectorDialog() {
        val date = currentState.dateSelectorState.run {
            LocalDate.of(year, month, day)
        }

        updateState {
            it.copy(
                    stepEditorState = currentState.stepEditorState.copy(selectedDate = date),
                    dateSelectorState = currentState.dateSelectorState.copy(
                            isDateSelectorVisible = false
                    )
            )
        }
    }

    private fun dismissDateSelectorDialog() {
        updateState {
            it.copy(
                    dateSelectorState = currentState.dateSelectorState.copy(
                            isDateSelectorVisible = false
                    )
            )
        }
    }

    private fun showExitDialog() {
        updateState {
            it.copy(showExitDialog = true)
        }
    }

    private fun confirmExitDialog() {
        updateState {
            it.copy(showExitDialog = false)

        }
        sendActionEvent(HomeActionEvent.CloseApp)
    }

    private fun dismissExitDialog() {
        updateState {
            it.copy(showExitDialog = false)
        }
    }
}
