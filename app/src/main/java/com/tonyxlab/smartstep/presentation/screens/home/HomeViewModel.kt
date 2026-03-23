package com.tonyxlab.smartstep.presentation.screens.home

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import timber.log.Timber

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    override fun onEvent(event: HomeUiEvent) {
        when (event) {

            is HomeUiEvent.ShowPermissionSheet -> {


                Timber.tag("PermHandler").i("VM event called for show permission sheet")
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

            HomeUiEvent.OpenPermissionsSettings,
            HomeUiEvent.Continue -> {
                updateState {
                    it.copy(isSheetVisible = false)
                }
            }

            HomeUiEvent.AllowAccess -> Unit
        }
    }

    private fun dismissDialog() {

        updateState { it.copy(isSheetVisible = false) }
    }
}
