@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report.handling

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.presentation.screens.report.model.MetricType
import com.tonyxlab.smartstep.utils.WeekUtils
import java.time.LocalDate

@Stable
data class ReportUiState(
    val weekDateState: WeekDateState = WeekDateState(),
    val activityReportState: ActivityReportState = ActivityReportState()
) : UiState {

    @Stable
    data class WeekDateState(
        val selectedWeekStartDate: LocalDate = WeekUtils.getFirstDayOfTheWeek(),
        val displayRange: String =
            WeekUtils.formatWeekRange(weekStartDate = selectedWeekStartDate),
        val isCurrentWeek: Boolean = true
    )

    @Stable
    data class ActivityReportState(
        val selectedMetricType: MetricType = MetricType.STEPS,
        val weeklyMetrics: List<DailyMetric> = emptyList()
    ) {

        val totalValue: Double
            get() = when (selectedMetricType) {
                MetricType.STEPS -> weeklyMetrics.sumOf { it.stepCount }
                        .toDouble()

                MetricType.CALORIES -> weeklyMetrics.sumOf { it.calories }
                        .toDouble()

                MetricType.TIME -> weeklyMetrics.sumOf { it.activeSeconds / 60 }
                        .toDouble()

                MetricType.DISTANCE -> weeklyMetrics.sumOf { it.distanceKm }
            }

        val averageValue: Double
            get() {
                val validValues = weeklyMetrics
                        .map { extractNumericalValue(it) }
                        .filter { it > 0.0 }

                validValues.ifEmpty { return 0.0 }

                return validValues.sum() / validValues.size
            }

        val totalDisplayValue: String
            get() = selectedMetricType.formatValue(totalValue)

        val averageDisplayValue: String
            get() = selectedMetricType.formatValue(averageValue)

        private fun extractNumericalValue(metric: DailyMetric): Double {
            return when (selectedMetricType) {
                MetricType.STEPS -> metric.stepCount.toDouble()
                MetricType.CALORIES -> metric.calories.toDouble()
                MetricType.TIME -> (metric.activeSeconds / 60.0)
                MetricType.DISTANCE -> metric.distanceKm
            }
        }
    }
}



