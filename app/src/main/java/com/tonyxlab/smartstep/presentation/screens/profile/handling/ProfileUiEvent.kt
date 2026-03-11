package com.tonyxlab.smartstep.presentation.screens.profile.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ProfileUiEvent : UiEvent {

    data object SkipOnboarding : ProfileUiEvent

    data object GenderSelectionVisibilityChange : ProfileUiEvent
    data object HeightPickerVisibilityChange : ProfileUiEvent
    data object WeightPickerVisibilityChange : ProfileUiEvent

    data class SelectGender(val gender: Gender) : ProfileUiEvent

    data class SelectHeightMode(val heightMode: HeightMode) : ProfileUiEvent
    data class OnCentimetersSelected(val value: Int) : ProfileUiEvent
    data class OnFeetSelected(val value: Int) : ProfileUiEvent
    data class OnInchesSelected(val value: Int) : ProfileUiEvent

    data class SelectWeightMode(val weightMode: WeightMode) : ProfileUiEvent
    data class OnKilosSelected(val value: Int) : ProfileUiEvent
    data class OnPoundsSelected(val value: Int) : ProfileUiEvent


    data object ConfirmHeightDialog : ProfileUiEvent
    data object CancelHeightDialog : ProfileUiEvent
    data object ConfirmWeightDialog : ProfileUiEvent
    data object CancelWeightDialog : ProfileUiEvent
}