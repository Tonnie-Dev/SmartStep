@file:OptIn(ExperimentalMaterial3Api::class)

package com.tonyxlab.smartstep.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.components.AppButton
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.StandardWheelPicker
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.HorizontalRoundedCornerShape28
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape28
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepGoalPicker(
    selectedStep: Int,
    isDeviceWide: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()

    if (isDeviceWide) {
        Dialog(onDismissRequest = { onEvent(HomeUiEvent.DismissStepGoalPicker) }) {
            GoalPickerContent(
                    modifier = modifier.clip(shape = MaterialTheme.shapes.RoundedCornerShape28),
                    selectedSteps = selectedStep,
                    onEvent = onEvent
            )
        }
    } else {

        ModalBottomSheet(
                modifier = modifier,
                onDismissRequest = { onEvent(HomeUiEvent.DismissStepGoalPicker) },
                sheetState = sheetState,
                shape = MaterialTheme.shapes.HorizontalRoundedCornerShape28,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = null
        ) {
            GoalPickerContent(
                    modifier = modifier,
                    selectedSteps = selectedStep,
                    onEvent = onEvent
            )
        }
    }
}

@Composable
private fun GoalPickerContent(
    selectedSteps: Int,
    modifier: Modifier = Modifier,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val stepsRange = remember {
        (1000..40000 step 1000).toList()
                .reversed()
    }

    Surface(modifier = modifier) {
        Column(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                        .padding(top = MaterialTheme.spacing.spaceTwelve)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                    text = stringResource(R.string.label_step_goal),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
            StandardWheelPicker(
                    values = stepsRange,
                    selectedValue = selectedSteps,
                    onValueSelected = { onEvent(HomeUiEvent.SelectStepGoal(it)) },
            )
            AppButton(
                    onClick = { onEvent(HomeUiEvent.SaveStepGoal) },
                    buttonText = stringResource(R.string.button_text_save)
            )

            TextButton(
                    onClick = { onEvent(HomeUiEvent.DismissStepGoalPicker) },

                    ) {
                Text(
                        text = stringResource(R.string.button_text_cancel),
                        style = MaterialTheme.typography.BodyLargeMedium,
                        color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun StepGoalPickerPreview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            StepGoalPicker(selectedStep = 3000, isDeviceWide = true) {}
        }
    }
}
