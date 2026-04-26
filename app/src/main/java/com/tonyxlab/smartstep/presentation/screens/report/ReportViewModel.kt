@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportActionEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.utils.WeekUtils
import java.time.LocalDate

typealias ReportBaseViewModel = BaseViewModel<ReportUiState, ReportUiEvent, ReportActionEvent>

class ReportViewModel : ReportBaseViewModel() {

    override val initialState: ReportUiState
        get() = ReportUiState()

    override fun onEvent(event: ReportUiEvent) {
        when (event) {
            ReportUiEvent.OnBackClick -> sendActionEvent(ReportActionEvent.NavigateBack)
            ReportUiEvent.ViewPreviousWeek -> viewPreviousWeek()
            ReportUiEvent.ViewNextWeek -> viewNextWeek()
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
                            currentWeekRange = WeekUtils.formatWeekRange(weekStartDate),
                            isCurrentWeek = WeekUtils.isCurrentWeek(weekStartDate)
                    )
            )
        }
    }

    private fun exitReport() {

    }
}

