package com.tonyxlab.smartstep.presentation.screens.home.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent{
    data object OpenAppSettings : HomeActionEvent
    data object RequestBatteryOptimization : HomeActionEvent
    data object CloseApp : HomeActionEvent
    data object StartStepCounterService : HomeActionEvent
    data object StopStepCounterService : HomeActionEvent
    data object NavigateToChat : HomeActionEvent
}
