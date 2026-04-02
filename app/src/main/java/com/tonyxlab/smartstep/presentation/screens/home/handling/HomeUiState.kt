@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import java.time.LocalDate

data class HomeUiState(
    val showExitDialog: Boolean = false,
    val currentSteps: Int = 0,
    val permissionUiState: PermissionUiState = PermissionUiState(),
    val stepGoalPickerState: StepGoalPickerState = StepGoalPickerState(),
    val stepEditorState: StepEditorState = StepEditorState()
) : UiState {

    @Stable
    data class PermissionUiState(
        val permissionSheetVisible: Boolean = false,
        val permissionSheetType: PermissionSheetType? = null,
        val physicalActivityPermissionRequested: Boolean = false,
        val isBackgroundAccessGranted: Boolean? = null
    )

    @Stable
    data class StepGoalPickerState(
        val pickerSheetVisible: Boolean = false,
        val selectedStepsGoal: Int = 2000
    )

    @Stable
    data class StepEditorState(
        val isStepEditorVisible: Boolean = false,
        val isDateSelectorVisible: Boolean = false,
        val selectedDate: LocalDate = LocalDate.now(),
        val stepsTextFieldState: TextFieldState = TextFieldState()
    )
}
