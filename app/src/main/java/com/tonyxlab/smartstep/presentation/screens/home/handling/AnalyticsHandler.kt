@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.presentation.screens.home.model.WeeklyDayStat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class AnalyticsHandler {
    fun populateStats(
        state: HomeUiState,
        weeklyMetrics: List<DailyMetric>,
        today: LocalDate = LocalDate.now()
    ): HomeUiState {

        val metricsByDate = weeklyMetrics.associateBy { dailyMetric ->
            dailyMetric.date
        }
        val stats = (6 downTo 0).map { pastDays ->

            val date = today.minusDays(pastDays.toLong())
            val metric = metricsByDate[date]
            
            WeeklyDayStat(
                    dayLabel = date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                    ),
                    steps = metric?.stepCount ?: 0,
                    goalAtThatDay = metric?.dailyStepGoal
                        ?: state.stepGoalSheetState.selectedStepsGoal,
            )
        }

        return state.copy(
                weeklyAnalyticState = state.weeklyAnalyticState.copy(
                        weeklyStats = stats
                )
        )
    }
}