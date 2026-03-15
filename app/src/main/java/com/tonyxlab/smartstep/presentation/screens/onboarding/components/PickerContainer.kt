package com.tonyxlab.smartstep.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.components.AppLabel
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyMediumRegular
import com.tonyxlab.smartstep.presentation.theme.EndVerticalRoundedCornerShape100
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape28
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.StartVerticalRoundedCornerShape100
import timber.log.Timber

@Composable
fun PickerContainer(
    modifier: Modifier = Modifier,
    pickerTitle: String,
    pickerDescription: String,
    unitOneText: String,
    unitTwoText: String,
    isUnitOneSelected: Boolean,
    onToggleUnitOne: () -> Unit,
    onToggleUnitTwo: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    wheelPicker: @Composable (() -> Unit)? = null

) {

    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.RoundedCornerShape28),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
    ) {
        Column(
                modifier = Modifier
                        .padding(
                                start = MaterialTheme.spacing.spaceTwelve * 2,
                                end = MaterialTheme.spacing.spaceTwelve * 2,
                                top = MaterialTheme.spacing.spaceTwelve * 2,
                                bottom = MaterialTheme.spacing.spaceTen * 2,
                        )
        ) {
            Text(
                    text = pickerTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceMedium),
                    text = pickerDescription,
                    style = MaterialTheme.typography.BodyMediumRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            UnitToggle(
                    modifier = Modifier,
                    unitOneText = unitOneText,
                    unitTwoText = unitTwoText,
                    onToggleUnitOne = onToggleUnitOne,
                    onToggleUnitTwo = onToggleUnitTwo,
                    isUnitOneSelected = isUnitOneSelected
            )

            // Wheel Picker
            wheelPicker?.invoke()

            // Action Buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text(
                            text = stringResource(R.string.button_text_cancel),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceMedium))
                TextButton(onClick = onConfirm) {
                    Text(
                            text = stringResource(R.string.button_text_ok),
                            style = MaterialTheme.typography.BodyLargeMedium.copy(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun UnitToggle(
    unitOneText: String,
    unitTwoText: String,
    onToggleUnitOne: () -> Unit,
    onToggleUnitTwo: () -> Unit,
    modifier: Modifier = Modifier,
    isUnitOneSelected: Boolean = true
) {

    Timber.tag("OnboardingVM")
            .i("U1 Selected is: $isUnitOneSelected")    // Unit Toggle

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.spaceTwelve * 2),
            horizontalArrangement = Arrangement.Center
    ) {

        AppLabel(
                modifier = Modifier.weight(1f),
                labelText = unitOneText,
                shape = MaterialTheme.shapes.StartVerticalRoundedCornerShape100,
                onClick = onToggleUnitOne,
                selected = isUnitOneSelected
        )
        AppLabel(
                modifier = Modifier.weight(1f),
                labelText = unitTwoText,
                shape = MaterialTheme.shapes.EndVerticalRoundedCornerShape100,
                onClick = onToggleUnitTwo,
                selected = isUnitOneSelected.not()
        )
    }
}

@Preview
@Composable
private fun PickerContainerPreviewSingleColumnStandard() {

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
                    onCancel = {},
                    onConfirm = {},
                    isUnitOneSelected = true,
                    wheelPicker = {
                        StandardWheelPicker(
                                modifier = Modifier,
                                selectedValue = 170,
                                valuesRange = 100..250,
                                onValueSelected = {}
                        )
                    }
            )
        }
    }
}
