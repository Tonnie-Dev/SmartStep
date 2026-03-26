package com.tonyxlab.smartstep.presentation.screens.home

import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(private val permPrefsDataStore: PermPrefsDataStore)
    : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    init {
        observePermissionStates()
    }

    private fun observePermissionStates() {
        viewModelScope.launch {
            combine(
                    permPrefsDataStore.physicalActivityPermissionRequested,
                    permPrefsDataStore.backgroundPermissionSheetShown
            ) { requested, shown ->
                Pair(requested, shown)
            }.collect { (requested, shown) ->
                updateState {
                    it.copy(
                            physicalActivityPermissionRequested = requested,
                            backgroundPermissionSheetShown = shown
                    )
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
            HomeUiEvent.BackgroundPermissionSheetShown -> backgroundPermissionSheetShown()
            HomeUiEvent.AllowAccess -> Unit
            HomeUiEvent.ExitApp -> Unit
            HomeUiEvent.FixCountIssue -> Unit
            HomeUiEvent.OpenNavigationDrawer -> Unit
            HomeUiEvent.OpenPersonalSettings -> Unit
            HomeUiEvent.SetStepGoal -> Unit
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

    private fun backgroundPermissionSheetShown() {
        viewModelScope.launch {
            permPrefsDataStore.setBackgroundPermissionSheetShown(true)
        }
    }
}
