@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import kotlinx.coroutines.launch
import timber.log.Timber

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(private val permPrefsDataStore: PermPrefsDataStore) : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
    }

    private fun observePermissionStates() {
        launch {

            permPrefsDataStore.physicalActivityPermissionRequested.collect { requested ->
                updateState {
                    it.copy(physicalActivityPermissionRequested = requested)
                }
            }
        }
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
            HomeUiEvent.OpenPersonalSettings -> Unit
            HomeUiEvent.SetStepGoal -> Unit
            is HomeUiEvent.BackgroundAccessChanged -> updateBackgroundAccessState(event.granted)
            HomeUiEvent.CancelStepsGoal -> Unit
            HomeUiEvent.DismissStepGoalPicker -> Unit
            HomeUiEvent.SaveStepGoal -> Unit
            is HomeUiEvent.SelectStepGoal -> Unit
        }
    }

    private fun showPermissionSheet(type: PermissionSheetType) {
        updateState {
            it.copy(
                    isSheetVisible = true,
                    permissionSheetType = type
            )
        }
    }

    private fun dismissPermissionDialog() {
        updateState {
            it.copy(
                    isSheetVisible = false,
                    permissionSheetType = null
            )
        }
    }

    private fun openPermissionsSettings() {
        updateState {
            it.copy(isSheetVisible = false)
        }
        sendActionEvent(HomeActionEvent.OpenAppSettings)
    }

    private fun handleContinue() {
        updateState { it.copy(isSheetVisible = false) }
        sendActionEvent(HomeActionEvent.RequestBatteryOptimization)
    }

    private fun physicalActivityPermissionRequested() {
        viewModelScope.launch {
            permPrefsDataStore.setPhysicalActivityPermissionRequested(true)
        }
    }

    private fun updateBackgroundAccessState(granted: Boolean) {
        updateState { it.copy(isBackgroundAccessGranted = granted) }
    }
}
