package com.tonyxlab.smartstep.presentation.screens.home.handling

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel

typealias HomeBaseViewModel = BaseViewModel<HomeUiState, HomeUiEvent, HomeActionEvent>

class HomeViewModel : HomeBaseViewModel() {

    override val initialState: HomeUiState
        get() = HomeUiState()

    override fun onEvent(event: HomeUiEvent) {
        // Handle events here
    }
}
