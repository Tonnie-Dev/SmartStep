package com.tonyxlab.smartstep.presentation.screens.home.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType

sealed interface HomeUiEvent : UiEvent {
    data object DismissPermissionDialog : HomeUiEvent
    data object AllowAccess : HomeUiEvent
    data object OpenPermissionsSettings : HomeUiEvent
    data object Continue : HomeUiEvent
    data object PhysicalActivityPermissionRequested : HomeUiEvent

    data class BackgroundAccessChanged(val granted: Boolean): HomeUiEvent
    data object ShowBackgroundPermissionSheet : HomeUiEvent

    data class ShowPermissionSheet(
        val type: PermissionSheetType
    ) : HomeUiEvent

    data object OpenNavigationDrawer : HomeUiEvent
    data object FixCountIssue : HomeUiEvent
    data object SetStepGoal : HomeUiEvent
    data object OpenPersonalSettings : HomeUiEvent
    data object ExitApp : HomeUiEvent

}
