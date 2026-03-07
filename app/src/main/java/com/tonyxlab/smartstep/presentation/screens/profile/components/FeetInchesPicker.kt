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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun FeetInchesPicker(
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
    val feetItems = remember(feetRange) {
        feetRange.map { it.toString() }
    }

    val inchItems = remember(inchesRange) {
        inchesRange.map { it.toString() }
    }

    val feetInitialIndex = (selectedFeet - feetRange.first)
            .coerceIn(0, feetItems.lastIndex)

    val inchesInitialIndex = (selectedInches - inchesRange.first)
            .coerceIn(0, inchItems.lastIndex)

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
            WheelPicker(
                    items = feetItems,
                    initialIndex = feetInitialIndex,
                    visibleItemsCount = visibleItemsCount,
                    itemHeight = itemHeight,
                    onItemSelected = { index ->
                        onFeetSelected(feetRange.first + index)
                    },
                    modifier = Modifier.weight(1f)
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
            WheelPicker(
                    items = inchItems,
                    initialIndex = inchesInitialIndex,
                    visibleItemsCount = visibleItemsCount,
                    itemHeight = itemHeight,
                    onItemSelected = { index ->
                        onInchesSelected(inchesRange.first + index)
                    },
                    modifier = Modifier.weight(1f)
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
private fun FeetInchesPickerPreview() {
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
                onSelectMeasurementMode = {},
                onCancel = {},
                onConfirm = {},
                wheelPicker = {
        FeetInchesPicker(
                selectedFeet = 5,
                selectedInches = 9,
                onFeetSelected = {},
                onInchesSelected = {},
                modifier = Modifier.fillMaxWidth()
        )})
    }
}}

