package com.tonyxlab.smartstep.presentation.screens.report

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportActionEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState

typealias ReportBaseViewModel = BaseViewModel<ReportUiState, ReportUiEvent, ReportActionEvent>

class ReportViewModel : ReportBaseViewModel() {

    override val initialState: ReportUiState
        get() = ReportUiState()

    override fun onEvent(event: ReportUiEvent) {
        when (event) {
            ReportUiEvent.OnBackClick -> sendActionEvent(ReportActionEvent.NavigateBack)
        }
    }

    private fun exitReport() {

    }
}
