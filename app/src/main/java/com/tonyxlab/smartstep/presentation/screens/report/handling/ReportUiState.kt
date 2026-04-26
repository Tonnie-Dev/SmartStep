package com.tonyxlab.smartstep.presentation.screens.report.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

@Stable
data class ReportUiState(
    val isLoading: Boolean = false
) : UiState
