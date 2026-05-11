package com.tonyxlab.smartstep.presentation.screens.report.model

import com.tonyxlab.smartstep.domain.model.DailyMetric
import java.time.LocalDate

data class ActivityUiItem(
    val dayName: String,
    val metricValue: String,
    val metricType: MetricType,
    val stepGoal: Int,
    val activityState: ActivityState
)

enum class ActivityState {
    HAS_DATA,
    IN_PROGRESS,
    NO_DATA
}

fun DailyMetric.toActivityState(metricType: MetricType): ActivityState {
    val today = LocalDate.now()
    return when {
        today == date -> ActivityState.IN_PROGRESS
        metricType.getNumericValue(this) > 0.0 -> ActivityState.HAS_DATA
        else -> ActivityState.NO_DATA
    }
}
