package com.tonyxlab.smartstep.presentation.screens.report.model

import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.utils.formatWithCommas
import java.util.Locale

enum class MetricType {
    STEPS,
    CALORIES,
    TIME,
    DISTANCE;

    fun displayName(): String {
        return when (this) {
            STEPS -> "Steps"
            CALORIES -> "Calories"
            TIME -> "Minutes"
            DISTANCE -> "Kilometers"
        }
    }

    fun unitName(isHeader: Boolean = false): String {
        return when (this) {
            STEPS -> "steps"
            CALORIES -> if (isHeader) "kcal"  else  "calories"
            TIME ->if (isHeader) "min"  else  "minutes"
            DISTANCE ->if (isHeader) "km"  else  "kilometers"
        }
    }

    fun getDisplayValue(metric: DailyMetric): String {

        return when (this) {
            STEPS -> metric.stepCount.toString()
            CALORIES -> metric.calories.toString()
            TIME -> (metric.activeSeconds / 60).toString()
            DISTANCE -> String.format(
                    Locale.getDefault(),
                    "%.1f",
                    metric.distanceKm
            )
        }

    }
    fun formatValue(value: Double): String {
        return when (this) {
            STEPS,
            CALORIES,
            TIME -> value.toInt().formatWithCommas()

            DISTANCE -> String.format(
                    Locale.getDefault(),
                    "%.1f",
                    value
            )
        }
    }

    fun getNumericValue(metric: DailyMetric): Double {
        return when (this) {
            STEPS -> metric.stepCount.toDouble()
            CALORIES -> metric.calories.toDouble()
            TIME -> metric.activeSeconds / 60.0
            DISTANCE -> metric.distanceKm
        }
    }
    override fun toString(): String {
        return unitName()
    }
}


