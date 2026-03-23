package com.tonyxlab.smartstep.presentation.screens.home

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

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

            HomeUiEvent.AllowAccess -> Unit
        }
    }

    private fun dismissDialog() {

        updateState { it.copy(isSheetVisible = false) }
    }
}
