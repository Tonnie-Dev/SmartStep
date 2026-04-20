@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.utils.AppDefaults
import java.time.LocalDate

data class HomeUiState(
    val currentSteps: Int = 0,
    val showExitDialog: Boolean = false,
    val showResetDialog: Boolean = false,

    val permissionUiState: PermissionUiState = PermissionUiState(),
    val stepGoalSheetState: StepGoalSheetState = StepGoalSheetState(),
    val stepEditorState: StepEditorState = StepEditorState(),
    val dateSelectorState: DateSelectorState = DateSelectorState(),
    val metricDataState: MetricDataState = MetricDataState(),
    val weeklyAnalyticState: WeeklyAnalyticState = WeeklyAnalyticState(),
    val insightMessageState: InsightMessageState = InsightMessageState()
) : UiState {

    @Stable
    data class PermissionUiState(
        val permissionSheetVisible: Boolean = false,
        val permissionSheetType: PermissionSheetType? = null,
        val physicalActivityPermissionRequested: Boolean = false,
        val isBackgroundAccessGranted: Boolean? = null
    )

    @Stable
    data class StepGoalSheetState(
        val pickerSheetVisible: Boolean = false,
        val selectedStepsGoal: Int = 2000
    )

    @Stable
    data class StepEditorState(
        val paused: Boolean = false,
        val isStepEditorVisible: Boolean = false,
        val selectedDate: LocalDate = LocalDate.now(),
        val stepsTextFieldState: TextFieldState = TextFieldState()
    )

    @Stable
    data class DateSelectorState(
        val isDateSelectorVisible: Boolean = false,
        val day: Int = LocalDate.now().dayOfMonth,
        val month: Int = LocalDate.now().monthValue,
        val year: Int = LocalDate.now().year
    )

    @Stable
    data class MetricDataState(
        val heightInCm: Int = AppDefaults.DEFAULT_HEIGHT_CM,
        val weightInKg: Int = AppDefaults.DEFAULT_WEIGHT_KG,
        val gender: Gender = Gender.MALE,
        val distance: Double = 0.0,
        val calories: Int = 0,
        val activityDurationSeconds: Int = 0,
        val time: Int = 0
    )

    @Stable
    data class WeeklyAnalyticState(
        val weeklyStats: List<DayStats> = emptyList(),
    ) {
        val dailyAverageSteps: Int
            get() = if (weeklyStats.isEmpty()) 0
            else weeklyStats.sumOf { it.steps } / weeklyStats.size
    }

    @Stable
    data class InsightMessageState(
        val isInsightLoading: Boolean = false,
        val isOnline: Boolean = false,
        val insightMessage: String = "",
    )
}

data class DayStats(
    val dayLabel: String,
    val steps: Int,
    val goalAtThatDay: Int,
    val isCurrentDay: Boolean = false
) {

    val progress: Float
        get() = if (goalAtThatDay > 0)
            (steps.toFloat() / goalAtThatDay).coerceAtMost(1f)
        else
            0f
}
