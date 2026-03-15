package com.tonyxlab.smartstep.presentation.screens.onboarding.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface OnboardingUiEvent : UiEvent {

    data object SkipOnboarding : OnboardingUiEvent
    data object CompleteOnBoarding : OnboardingUiEvent

    data object HeightPickerVisibilityChange : OnboardingUiEvent
    data object WeightPickerVisibilityChange : OnboardingUiEvent

    data class SelectGender(val gender: Gender) : OnboardingUiEvent

    data class SelectHeightMode(val heightMode: HeightMode) : OnboardingUiEvent
    data class OnCentimetersSelected(val value: Int) : OnboardingUiEvent
    data class OnFeetSelected(val value: Int) : OnboardingUiEvent
    data class OnInchesSelected(val value: Int) : OnboardingUiEvent

    data class SelectWeightMode(val weightMode: WeightMode) : OnboardingUiEvent
    data class OnKilosSelected(val value: Int) : OnboardingUiEvent
    data class OnPoundsSelected(val value: Int) : OnboardingUiEvent


    data object ConfirmHeightDialog : OnboardingUiEvent
    data object CancelHeightDialog : OnboardingUiEvent
    data object ConfirmWeightDialog : OnboardingUiEvent
    data object CancelWeightDialog : OnboardingUiEvent
}