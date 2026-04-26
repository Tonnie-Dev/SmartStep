package com.tonyxlab.smartstep.presentation.screens.report.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ReportUiEvent : UiEvent {
    data object OnBackClick : ReportUiEvent
    data object ViewPreviousWeek : ReportUiEvent
    data object ViewNextWeek : ReportUiEvent
}
