package com.tonyxlab.smartstep.presentation.screens.home.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType

sealed interface HomeUiEvent : UiEvent {
    data object DismissPermissionDialog : HomeUiEvent
    data object AllowAccess : HomeUiEvent
    data object OpenPermissionsSettings : HomeUiEvent
    data object Continue : HomeUiEvent
    data object PhysicalActivityPermissionRequested : HomeUiEvent

    data class BackgroundAccessChanged(val granted: Boolean) : HomeUiEvent
    data object ShowBackgroundPermissionSheet : HomeUiEvent

    data class ShowPermissionSheet(
        val type: PermissionSheetType
    ) : HomeUiEvent

    // Navigation Drawer Events
    data object OpenNavigationDrawer : HomeUiEvent
    data object FixCountIssue : HomeUiEvent
    data object ShowStepGoalSheet : HomeUiEvent
    data object OpenPersonalSettings : HomeUiEvent
    data object EditSteps : HomeUiEvent
    data object ResetSteps : HomeUiEvent
    data object ShowExitDialog : HomeUiEvent

    // Steps Goal
    data class SelectStepGoal(val selectedSteps: Int) : HomeUiEvent
    data object DismissStepGoalSheet : HomeUiEvent
    data object SaveStepGoal : HomeUiEvent

    // Steps Editor Dialog
    data class OnDaySelected(val value: Int) : HomeUiEvent
    data class OnMonthSelected(val value: Int) : HomeUiEvent
    data class OnYearSelected(val value: Int) : HomeUiEvent
    data object ConfirmStepEditorValues : HomeUiEvent
    data object ConfirmDateSelection:HomeUiEvent
    data object DismissStepEditor : HomeUiEvent
    data object DismissDateSelector : HomeUiEvent
    data object ShowDateSelector : HomeUiEvent

    // Motion
    data object OnMotionDetected : HomeUiEvent

    // Reset Dialog
    data object ConfirmResetDialog : HomeUiEvent
    data object DismissResetDialog : HomeUiEvent

    // Exit Dialog
    data object ConfirmExitDialog : HomeUiEvent
    data object DismissExitDialog : HomeUiEvent
}
