@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

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
            HomeUiEvent.ShowBackgroundPermissionSheet -> Unit
            HomeUiEvent.AllowAccess -> Unit
            HomeUiEvent.ExitApp -> Unit
            HomeUiEvent.FixCountIssue -> showPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS)
            HomeUiEvent.OpenNavigationDrawer -> Unit
            HomeUiEvent.OpenPersonalSettings -> openPersonalSettings()
            HomeUiEvent.ShowStepGoalPicker -> showStepGoalPicker()
            is HomeUiEvent.BackgroundAccessChanged -> updateBackgroundAccessState(event.granted)
            HomeUiEvent.DismissStepGoalPicker -> dismissStepGoalPicker()
            HomeUiEvent.SaveStepGoal -> saveStepGoalPicker()
            is HomeUiEvent.SelectStepGoal -> onSelectStepGoal(event.selectedSteps)
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

    private fun showStepGoalPicker() {
        updateState {
            it.copy(
                    stepGoalPickerState = currentState.stepGoalPickerState
                            .copy(pickerSheetVisible = true)
            )
        }
    }

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
}
