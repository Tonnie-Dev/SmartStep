@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.domain.repository.MetricsRepository
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportActionEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.screens.report.model.MetricType
import com.tonyxlab.smartstep.utils.WeekUtils
import kotlinx.coroutines.Job
import java.time.LocalDate

typealias ReportBaseViewModel = BaseViewModel<ReportUiState, ReportUiEvent, ReportActionEvent>

class ReportViewModel(
    private val metricsRepository: MetricsRepository
) : ReportBaseViewModel() {

    override val initialState: ReportUiState
        get() = ReportUiState()

    private var weeklyMetricsJob: Job? = null

    init {
        observeWeeklyMetric()
    }

    override fun onEvent(event: ReportUiEvent) {
        when (event) {
            ReportUiEvent.OnBackClick -> exitReport()

            ReportUiEvent.ViewPreviousWeek -> viewPreviousWeek()

            ReportUiEvent.ViewNextWeek -> viewNextWeek()

            is ReportUiEvent.SelectMetricType -> {
                selectMetricType(event.metricType)
            }
        }
    }

    private fun observeWeeklyMetric() {
        weeklyMetricsJob?.cancel()

        val weekStartDate = currentState.weekDateState.selectedWeekStartDate

        weeklyMetricsJob = launch {
            metricsRepository
                    .getWeeklyMetrics(weekStartDate)
                    .collect { metrics ->

                        val fullWeekMetrics = buildFullWeek(weekStartDate, metrics)
                        updateState { state ->
                            state.copy(
                                    activityReportState = state.activityReportState.copy(
                                            weeklyMetrics = fullWeekMetrics
                                    )
                            )
                        }
                    }
        }
    }

    private fun viewPreviousWeek() {
        val newWeekStartDate =
            currentState.weekDateState.selectedWeekStartDate.minusWeeks(1)

        updateSelectedWeek(newWeekStartDate)
    }

    private fun viewNextWeek() {
        if (currentState.weekDateState.isCurrentWeek) return

        val newWeekStartDate =
            currentState.weekDateState.selectedWeekStartDate.plusWeeks(1)

        updateSelectedWeek(newWeekStartDate)
    }

    private fun updateSelectedWeek(weekStartDate: LocalDate) {
        updateState {
            it.copy(
                    weekDateState = ReportUiState.WeekDateState(
                            selectedWeekStartDate = weekStartDate,
                            displayRange = WeekUtils.formatWeekRange(weekStartDate),
                            isCurrentWeek = WeekUtils.isCurrentWeek(weekStartDate)
                    )
            )
        }

        observeWeeklyMetric()
    }

    private fun selectMetricType(metricType: MetricType) {
        updateState {
            it.copy(
                    activityReportState = it.activityReportState.copy(
                            selectedMetricType = metricType
                    )
            )
        }
    }

    private fun buildFullWeek(weekStartDate: LocalDate, weekMetrics: List<DailyMetric>): List<DailyMetric> {

        val metricsByDate = weekMetrics.associateBy { it.date }

       return (0..6).map { dayOffset ->

            val date = weekStartDate.plusDays(dayOffset.toLong())

            metricsByDate[date] ?: DailyMetric(
                    date = date,
                    stepCount = 0,
                    dailyStepGoal = 0,
                    calories = 0,
                    activeSeconds = 0,
                    distanceKm = 0.0
            )

        }

    }

    private fun exitReport() {
        sendActionEvent(ReportActionEvent.NavigateBack)
    }
}