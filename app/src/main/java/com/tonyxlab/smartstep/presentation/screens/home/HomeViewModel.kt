package com.tonyxlab.smartstep.presentation.screens.home

import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel(
    private val onboardingDataStore: OnboardingDataStore,
    private val permPrefsDataStore: PermPrefsDataStore
) : HomeBaseViewModel() {

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
            is HomeUiEvent.ShowPermissionSheet -> {
                updateState {
                    it.copy(
                            isSheetVisible = true,
                            permissionSheetType = event.type
                    )
                }
            }

            HomeUiEvent.DismissPermissionDialog -> {

                updateState {
                    it.copy(
                            isSheetVisible = false,
                            permissionSheetType = null
                    )
                }
            }

            HomeUiEvent.OpenPermissionsSettings -> {
                updateState {
                    it.copy(
                            isSheetVisible = false
                    )
                }
                sendActionEvent(HomeActionEvent.OpenAppSettings)
            }

            HomeUiEvent.Continue -> {
                updateState {
                    it.copy(isSheetVisible = false)
                }
                sendActionEvent(HomeActionEvent.RequestBatteryOptimization)
            }

            HomeUiEvent.PhysicalActivityPermissionRequested -> {
                viewModelScope.launch {
                    permPrefsDataStore.setPhysicalActivityPermissionRequested(true)
                }
            }

            HomeUiEvent.BackgroundPermissionSheetShown -> {
                viewModelScope.launch {
                    permPrefsDataStore.setBackgroundPermissionSheetShown(true)
                }
            }

            HomeUiEvent.AllowAccess -> Unit
        }
    }
}
