package com.tonyxlab.smartstep.presentation.screens.home.handling

import androidx.annotation.StringRes
import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent

sealed interface HomeActionEvent : ActionEvent {
    data object OpenAppSettings : HomeActionEvent
    data object RequestBatteryOptimization : HomeActionEvent
    data object CloseApp : HomeActionEvent
    data object StartStepCounterService : HomeActionEvent
    data object StopStepCounterService : HomeActionEvent
    data object NavigateToChat : HomeActionEvent
    data object NavigateToReports : HomeActionEvent
    data class ShowToastMessage(@StringRes val messageRes: Int) : HomeActionEvent
}
