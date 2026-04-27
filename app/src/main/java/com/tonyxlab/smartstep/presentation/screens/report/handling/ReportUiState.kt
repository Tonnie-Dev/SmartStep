@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report.handling

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.utils.WeekUtils
import java.time.LocalDate

@Stable
data class ReportUiState(
    val stepCount: Int = 1300,
    val averageSteps: Int = 647,
    val calories: Int = 90,
    val distanceInKms: Double = 3.8,
    val timeInMinutes: Int = 81,
    val selectedMetricType: MetricType = MetricType.STEPS,
    val weekDateState: WeekDateState = WeekDateState()
) : UiState {

    @Stable
    data class WeekDateState(
        val selectedWeekStartDate: LocalDate = WeekUtils.getFirstDayOfTheWeek(),
        val currentWeekRange: String =
            WeekUtils.formatWeekRange(weekStartDate = selectedWeekStartDate),
        val isCurrentWeek: Boolean = true
    )

}

enum class MetricType { STEPS, CALORIES, TIME, DISTANCE;
    override fun toString(): String {
        return when(this){
            STEPS -> "Steps"
            CALORIES -> "calories"
            TIME -> "minutes"
            DISTANCE -> "kilometers"
        }
    }
}


/*
sealed interface MetricType{
    data class Steps(val stepCount: Int): MetricType
    data class Calories(val calories: Int): MetricType
    data class Time(val minutes: Int): MetricType
    data class Distance(val kms: Double): MetricType
}*/
