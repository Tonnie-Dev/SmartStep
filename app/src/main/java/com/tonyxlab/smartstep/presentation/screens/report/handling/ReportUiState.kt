package com.tonyxlab.smartstep.presentation.screens.report.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

@Stable
data class ReportUiState(
    val stepCount: Int = 1300,
    val averageSteps:Int = 647,
    val calories: Int = 90,
    val distanceInKms: Double = 3.8,
    val timeInMinutes: Int = 81,
    val selectedMetricType: MetricType = MetricType.STEPS

) : UiState {

}

enum class MetricType { STEPS, CALORIES, TIME, DISTANCE }
