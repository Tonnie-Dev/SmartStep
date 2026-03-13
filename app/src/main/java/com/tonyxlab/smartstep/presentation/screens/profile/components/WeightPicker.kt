package com.tonyxlab.smartstep.presentation.screens.profile.components

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
import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileUiEvent
import com.tonyxlab.smartstep.presentation.screens.profile.handling.WeightMode
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun WeightPicker(
    modifier: Modifier = Modifier,
    selectedKilos: Int = 60,
    kiloRange: IntRange = 30..200,
    selectedPounds: Int = 143,
    poundsRange: IntRange = 66..440,
    weightMode: WeightMode = WeightMode.KILOS,
    onEvent: (ProfileUiEvent) -> Unit
) {

    Dialog(
            onDismissRequest = { onEvent(ProfileUiEvent.CancelWeightDialog) },
            properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        PickerContainer(
                modifier = modifier,
                pickerTitle = stringResource(id = R.string.label_text_weight),
                pickerDescription = stringResource(id = R.string.caption_text_calculate_calories),
                unitOneText = stringResource(id = R.string.label_text_kg),
                unitTwoText = stringResource(id = R.string.label_text_lbs),
                onToggleUnitOne = { onEvent(ProfileUiEvent.SelectWeightMode(WeightMode.KILOS)) },
                onToggleUnitTwo = { onEvent(ProfileUiEvent.SelectWeightMode(WeightMode.POUNDS)) },
                onConfirm = { onEvent(ProfileUiEvent.ConfirmWeightDialog) },
                onCancel = { onEvent(ProfileUiEvent.CancelHeightDialog) },
                isUnitOneSelected = weightMode == WeightMode.KILOS,
                wheelPicker = {

                    when (weightMode) {

                        WeightMode.KILOS -> {
                            StandardWheelPicker(
                                    modifier = Modifier,
                                    selectedValue = selectedKilos,
                                    valuesRange = kiloRange,
                                    onValueSelected = {
                                        onEvent(ProfileUiEvent.OnKilosSelected(value = it))
                                    },
                            )
                        }

                        WeightMode.POUNDS -> {

                            StandardWheelPicker(
                                    modifier = Modifier,
                                    selectedValue = selectedPounds,
                                    valuesRange = poundsRange,
                                    onValueSelected = {
                                        onEvent(ProfileUiEvent.OnPoundsSelected(value = it))
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
private fun HeightPicker_Preview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(16.dp),
                contentAlignment = Alignment.Center
        ) {
            WeightPicker(modifier = Modifier) {}
        }
    }
}


