package com.tonyxlab.smartstep.presentation.screens.report.model

enum class MetricType {
    STEPS, CALORIES, TIME, DISTANCE;

    override fun toString(): String {
        return when (this) {
            STEPS -> "Steps"
            CALORIES -> "calories"
            TIME -> "minutes"
            DISTANCE -> "kilometers"
        }
    }
}

data class DailyMetric(
    val stepCount:Int,
    val calories:Int,
    val time: Int,
    val distance: Double
)
