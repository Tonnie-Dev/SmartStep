@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.utils.UnitConverter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class StepsHandler {

    // Calculations
    fun recalculateMetrics(state: HomeUiState): HomeUiState {
        val heightInCm = state.metricDataState.heightInCm
        val weightInKg = state.metricDataState.weightInKg
        val activitySeconds = state.metricDataState.activityDurationSeconds

        val distance = UnitConverter.stepsToKm(
                heightInCm = heightInCm,
                steps = state.currentSteps
        )
        val caloriesBurnt = UnitConverter.stepsToCalories(
                steps = state.currentSteps,
                weight = weightInKg,
                gender = state.metricDataState.gender
        )

        val activityDisplayMinutes = UnitConverter.secondsToDisplayMinutes(activitySeconds)
        return state.copy(
                metricDataState = state.metricDataState.copy(
                        distance = distance,
                        calories = caloriesBurnt,
                        time = activityDisplayMinutes
                )
        )
    }

    // Motion
    fun incrementSteps(state: HomeUiState): HomeUiState {
        if (state.stepEditorState.paused) return state
        return state.copy(currentSteps = state.currentSteps + 1)
    }

    // Timer
    fun addActivitySecond(state: HomeUiState): HomeUiState {
        val updatedSeconds = state.metricDataState.activityDurationSeconds + 1

        return state.copy(
                metricDataState = state.metricDataState.copy(
                        activityDurationSeconds = updatedSeconds,
                        time = UnitConverter.secondsToDisplayMinutes(updatedSeconds)
                )
        )
    }

    fun resetActivityTime(state: HomeUiState): HomeUiState {
        return state.copy(
                metricDataState = state.metricDataState.copy(
                        activityDurationSeconds = 0,
                        time = 0
                )
        )
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


