package com.tonyxlab.smartstep.presentation.screens.report.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent

sealed interface ReportActionEvent : ActionEvent {
    data object NavigateBack : ReportActionEvent
}
