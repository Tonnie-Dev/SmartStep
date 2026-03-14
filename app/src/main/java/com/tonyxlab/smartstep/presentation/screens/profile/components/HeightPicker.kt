package com.tonyxlab.smartstep.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.screens.profile.handling.HeightMode
import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileUiEvent
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun HeightPicker(
    modifier: Modifier = Modifier,
    selectedCentimeter: Int,
    selectedFeet: Int,
    selectedInches: Int,
    centimeterRange: IntRange = 100..250,
    feetRange: IntRange = 3..8,
    inchesRange: IntRange = 0..11,
    heightMode: HeightMode = HeightMode.CENTIMETERS,
    onEvent: (ProfileUiEvent) -> Unit
) {

    Dialog(
            onDismissRequest = { onEvent(ProfileUiEvent.CancelHeightDialog) },
            properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        PickerContainer(
                modifier = modifier,
                pickerTitle = stringResource(id = R.string.label_text_height),
                pickerDescription = stringResource(id = R.string.caption_text_calculate_distance),
                unitOneText = stringResource(id = R.string.label_text_cm),
                unitTwoText = stringResource(id = R.string.label_text_ft_in),
                onToggleUnitOne = { onEvent(ProfileUiEvent.SelectHeightMode(HeightMode.CENTIMETERS)) },
                onToggleUnitTwo = { onEvent(ProfileUiEvent.SelectHeightMode(HeightMode.FEET_INCHES)) },
                onConfirm = { onEvent(ProfileUiEvent.ConfirmHeightDialog) },
                onCancel = { onEvent(ProfileUiEvent.CancelHeightDialog) },
                isUnitOneSelected = heightMode == HeightMode.CENTIMETERS,
                wheelPicker = {

                    when (heightMode) {
                        HeightMode.CENTIMETERS -> {
                            StandardWheelPicker(
                                    modifier = Modifier,
                                    selectedValue = selectedCentimeter,
                                    valuesRange = centimeterRange,
                                    onValueSelected = {
                                        onEvent(ProfileUiEvent.OnCentimetersSelected(value = it))
                                    },
                            )
                        }

                        HeightMode.FEET_INCHES -> {
                            FeetInchesWheelPicker(
                                    modifier = Modifier,
                                    selectedFeet = selectedFeet,
                                    selectedInches = selectedInches,
                                    onFeetSelected = {
                                        onEvent(ProfileUiEvent.OnFeetSelected(value = it))
                                    },
                                    onInchesSelected = {
                                        onEvent(ProfileUiEvent.OnInchesSelected(value = it))
                                    },
                                    feetRange = feetRange,
                                    inchesRange = inchesRange
                            )
                        }
                    }
                }
        )
    }
}

@Composable
fun FeetInchesWheelPicker(
    selectedFeet: Int,
    selectedInches: Int,
    onFeetSelected: (Int) -> Unit,
    onInchesSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    feetRange: IntRange = 3..8,
    inchesRange: IntRange = 0..11,
    visibleItemsCount: Int = 5,
    itemHeight: Dp = 48.dp,
) {

    val feetInitialIndex = (selectedFeet - feetRange.first)
            .coerceIn(0, feetRange.toList().lastIndex)

    val inchesInitialIndex = (selectedInches - inchesRange.first)
            .coerceIn(0, inchesRange.toList().lastIndex)

    Box(
            modifier = modifier
                    .fillMaxWidth()
                    .height(itemHeight * visibleItemsCount),
            contentAlignment = Alignment.Center
    ) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {

            // Feet wheel
            StandardWheelPicker(
                    modifier = modifier.weight(1f),
                    valuesRange = feetRange,
                    selectedValue = selectedFeet,
                    visibleItemsCount = visibleItemsCount,
                    itemHeight = itemHeight,
                    onValueSelected = onFeetSelected
            )

            // Static "ft" label aligned with selected center row
            Box(
                    modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiary)
                            .width(40.dp)
                            .height(itemHeight),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = "ft",
                        style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Inches wheel
            StandardWheelPicker(
                    modifier = modifier.weight(1f),
                    valuesRange = inchesRange,
                    selectedValue = selectedInches,
                    visibleItemsCount = visibleItemsCount,
                    itemHeight = itemHeight,
                    onValueSelected = onInchesSelected
            )

            // Static "in" label aligned with selected center row
            Box(
                    modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiary)
                            .width(40.dp)
                            .height(itemHeight),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = "in",
                        style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
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
            HeightPicker(
                    selectedCentimeter = 175,
                    selectedFeet = 5,
                    selectedInches = 9,
                    heightMode = HeightMode.CENTIMETERS,
                    onEvent = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEAF3FF)
@Composable
private fun FeetInchesWheelPicker_Preview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(16.dp),
                contentAlignment = Alignment.Center
        ) {
            PickerContainer(
                    pickerTitle = stringResource(id = R.string.label_text_height),
                    pickerDescription = stringResource(id = R.string.caption_text_calculate_distance),
                    unitOneText = stringResource(id = R.string.label_text_cm),
                    unitTwoText = stringResource(id = R.string.label_text_ft_in),
                    onToggleUnitOne = {},
                    onToggleUnitTwo = {},
                    onConfirm = {},
                    onCancel = {},
                    isUnitOneSelected = true,
                    wheelPicker = {
                        HeightPicker(
                                selectedCentimeter = 170,
                                selectedFeet = 5,
                                selectedInches = 9,
                                heightMode = HeightMode.FEET_INCHES,
                                onEvent = {}
                        )
                    }
            )
        }
    }
}


