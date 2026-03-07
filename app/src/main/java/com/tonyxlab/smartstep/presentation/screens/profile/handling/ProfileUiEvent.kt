package com.tonyxlab.smartstep.presentation.screens.profile.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ProfileUiEvent : UiEvent {
    data object OnCancel : ProfileUiEvent
    data object OnConfirm : ProfileUiEvent
    data class SelectWeightMode(val weightMode: WeightMode) : ProfileUiEvent
    data class SelectHeightMode(val heightMode: HeightMode) : ProfileUiEvent
}