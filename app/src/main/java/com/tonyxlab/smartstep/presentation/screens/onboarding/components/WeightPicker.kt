package com.tonyxlab.smartstep.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.WeightMode
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun WeightPicker(
    modifier: Modifier = Modifier,
    selectedKilos: Int,
    selectedPounds: Int,
    kilosRange: IntRange = 30..200,
    poundsRange: IntRange = 66..440,
    weightMode: WeightMode = WeightMode.KGS,
    onEvent: (OnboardingUiEvent) -> Unit
) {

    Dialog(
            onDismissRequest = { onEvent(OnboardingUiEvent.CancelWeightDialog) },
            properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        PickerContainer(
                modifier = modifier,
                pickerTitle = stringResource(id = R.string.label_text_weight),
                pickerDescription = stringResource(id = R.string.caption_text_calculate_distance),
                unitOneText = stringResource(id = R.string.label_text_kg),
                unitTwoText = stringResource(id = R.string.label_text_lbs),
                onToggleUnitOne = { onEvent(OnboardingUiEvent.SelectWeightMode(WeightMode.KGS)) },
                onToggleUnitTwo = { onEvent(OnboardingUiEvent.SelectWeightMode(WeightMode.LBS)) },
                onConfirm = { onEvent(OnboardingUiEvent.ConfirmWeightDialog) },
                onCancel = { onEvent(OnboardingUiEvent.CancelWeightDialog) },
                isUnitOneSelected = weightMode == WeightMode.KGS,
                wheelPicker = {

                    when (weightMode) {
                        WeightMode.KGS -> {
                            StandardWheelPicker(
                                    modifier = Modifier,
                                    selectedValue = selectedKilos,
                                    values = kilosRange.toList(),
                                    onValueSelected = {
                                        onEvent(OnboardingUiEvent.OnKilosSelected(value = it))
                                    },
                            )
                        }

                        WeightMode.LBS -> {
                            StandardWheelPicker(
                                    modifier = Modifier,
                                    selectedValue = selectedPounds,
                                    values = poundsRange.toList(),
                                    onValueSelected = {
                                        onEvent(OnboardingUiEvent.OnPoundsSelected(value = it))
                                    },
                            )
                        }
                    }
                }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF3FF)
@Composable
private fun WeightPicker_Preview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(16.dp),
                contentAlignment = Alignment.Center
        ) {
            WeightPicker(
                    selectedKilos = 70,
                    selectedPounds = 154,
                    weightMode = WeightMode.KGS,
                    onEvent = {}
            )
        }
    }
}
