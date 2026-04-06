@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class AnalyticsHandler {


    fun populateStats(state: HomeUiState): HomeUiState {

        val stats = (6 downTo 0).map { pastDays ->

            val date = LocalDate.now()
                    .minusDays(pastDays.toLong())
            val isToday = pastDays == 0
            DayStats(
                    dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    steps = 1500,
                    goalAtThatDay = if (isToday) state.stepGoalSheetState.selectedStepsGoal else 4000,
                    isCurrentDay = isToday
            )

        }

        return state.copy(
                weeklyAnalyticState = state.weeklyAnalyticState.copy(
                        weeklyStats = stats
                )
        )
    }
}