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

    // Navigation Drawer Events
    data object OpenNavigationDrawer : HomeUiEvent
    data object FixCountIssue : HomeUiEvent
    data object ShowStepGoalPicker : HomeUiEvent
    data object OpenPersonalSettings : HomeUiEvent
    data object ShowExitDialog : HomeUiEvent

    // Steps Goal
    data class SelectStepGoal(val selectedSteps: Int): HomeUiEvent
    data object DismissStepGoalPicker: HomeUiEvent
    data object SaveStepGoal: HomeUiEvent

    // Exit Dialog
    data object ConfirmExitDialog : HomeUiEvent
    data object DismissExitDialog : HomeUiEvent

}
