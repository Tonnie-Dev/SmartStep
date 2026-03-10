package com.tonyxlab.smartstep.presentation.screens.profile.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ProfileUiEvent : UiEvent {
   data class SelectGender(val gender: Gender) : ProfileUiEvent


    data class SelectWeightMode(val weightMode: WeightMode) : ProfileUiEvent
    data class SelectHeightMode(val heightMode: HeightMode) : ProfileUiEvent
    data object HeightPickerVisibilityChanged: ProfileUiEvent

    data class OnCentimetersSelected(val value: Int): ProfileUiEvent
    data class OnFeetSelected(val value: Int): ProfileUiEvent
    data class OnInchesSelected(val value: Int): ProfileUiEvent

    data object ConfirmHeightDialog : ProfileUiEvent
    data object CancelHeightDialog : ProfileUiEvent
    data object OnConfirmWeightDialog : ProfileUiEvent
    data object CancelWeightDialog : ProfileUiEvent
}