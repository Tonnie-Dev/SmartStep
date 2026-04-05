@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class StepsHandler {

    // Motion
    fun incrementSteps(state: HomeUiState): HomeUiState {
        if (state.stepEditorState.paused) return state
        return state.copy(currentSteps = state.currentSteps + 1)
    }

    // Step Goal Picker
    fun onSelectStepGoal(state: HomeUiState, selectedSteps: Int): HomeUiState {
        return state.copy(
                stepGoalSheetState = state.stepGoalSheetState
                        .copy(selectedStepsGoal = selectedSteps)
        )
    }

    fun closeStepGoalSheet(state: HomeUiState): HomeUiState {
        return state.copy(
                stepGoalSheetState = state.stepGoalSheetState
                        .copy(pickerSheetVisible = false)
        )
    }

    // Step Editor
    fun showStepEditor(state: HomeUiState): HomeUiState {
        return state.copy(
                stepEditorState = state.stepEditorState.copy(
                        isStepEditorVisible = true
                )
        )
    }

    fun dismissStepEditor(
        initialState: HomeUiState,
        state: HomeUiState
    ): HomeUiState {
        return state.copy(
                stepEditorState = initialState.stepEditorState,
                dateSelectorState = initialState.dateSelectorState
        )
    }

    fun confirmStepEditor(state: HomeUiState): HomeUiState {
        val stepsText = state.stepEditorState.stepsTextFieldState.text
                .toString()
                .trim()

        val steps = stepsText.toIntOrNull() ?: 0

        return state.copy(
                currentSteps = steps,
                stepEditorState = state.stepEditorState.copy(
                        isStepEditorVisible = false
                )
        )
    }

    // Date Selector
    fun showDateSelector(state: HomeUiState): HomeUiState {
        return state.copy(
                dateSelectorState = state.dateSelectorState.copy(
                        isDateSelectorVisible = true
                )
        )
    }

    fun dismissDateSelector(state: HomeUiState): HomeUiState {
        return state.copy(
                dateSelectorState = state.dateSelectorState.copy(
                        isDateSelectorVisible = false
                )
        )
    }

    fun onDaySelected(state: HomeUiState, day: Int): HomeUiState {
        return state.copy(
                dateSelectorState = state.dateSelectorState.copy(
                        day = day
                )
        )
    }

    fun onMonthSelected(state: HomeUiState, month: Int): HomeUiState {
        return state.copy(
                dateSelectorState = state.dateSelectorState.copy(
                        month = month
                )
        )
    }

    fun onYearSelected(state: HomeUiState, year: Int): HomeUiState {
        return state.copy(
                dateSelectorState = state.dateSelectorState.copy(
                        year = year
                )
        )
    }

    fun confirmDateSelection(state: HomeUiState): HomeUiState {
        val selectedDate = runCatching {
            state.dateSelectorState.run {
                LocalDate.of(year, month, day)
            }
        }.getOrNull() ?: return state

        return state.copy(
                stepEditorState = state.stepEditorState.copy(
                        selectedDate = selectedDate
                ),
                dateSelectorState = state.dateSelectorState.copy(
                        isDateSelectorVisible = false
                )
        )
    }

    fun pauseStepCounting(state: HomeUiState): HomeUiState {

        val currentPauseState = state.stepEditorState.paused
        return state.copy(stepEditorState = state.stepEditorState.copy(paused = !currentPauseState))
    }
}


