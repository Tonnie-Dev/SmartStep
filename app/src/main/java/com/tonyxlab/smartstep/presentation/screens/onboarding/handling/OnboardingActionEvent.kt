package com.tonyxlab.smartstep.presentation.screens.onboarding.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent

sealed interface OnboardingActionEvent : ActionEvent {
    data object NavigateToHome : OnboardingActionEvent
}
