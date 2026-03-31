@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.OnboardingSelectionField
import com.tonyxlab.smartstep.presentation.theme.BodyMediumRegular
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.toDisplayDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape28

@Composable
fun StepsEditor(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.RoundedCornerShape28
    /*onDismiss: () -> Unit,
    onSave: (date: String, steps: String) -> Unit,
    initialDate: String = "2025/11/30",
    initialSteps: String = "0"*/
) {
        var date by remember { mutableStateOf("31/03/2026") }
        var steps by remember { mutableStateOf("0") }

    Dialog(onDismissRequest = {onEvent(HomeUiEvent.DismissStepEditor)}) {
        Surface(
                modifier = modifier.fillMaxWidth(),
                shape = shape,
                color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
                            .padding(top = MaterialTheme.spacing.spaceTwelve * 2)
                            .padding(bottom = MaterialTheme.spacing.spaceTen)
            ) {
                Text(
                        text = stringResource(id = R.string.header_text_edit_steps),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

                Text(
                        text = stringResource(id = R.string.caption_text_recalculation),
                        style = MaterialTheme.typography.BodyMediumRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

                // Date Field
                EditorInputField(
                    label = stringResource(id = R.string.label_text_date),
                    value = uiState.stepEditorState.selectedDate.toDisplayDate(),
                    onValueChange = { date = it },
                    isDropDown = true,
                    onClick = {  onEvent(HomeUiEvent.ShowDateSelector)  }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve))

                // Steps Field
                EditorInputField(
                        label = stringResource(id = R.string.label_steps),
                        value = uiState.stepEditorState.stepsToAdd.toString(),
                        onValueChange = { steps = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {onEvent(HomeUiEvent.DismissStepEditor)}) {
                        Text(
                                text = stringResource(id = R.string.button_text_cancel),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))
                    TextButton(onClick = { onEvent(HomeUiEvent.SaveStepEditorValues )}) {
                        Text(
                                text = stringResource(id = R.string.button_text_save),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDropDown: Boolean = false,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.RoundedCornerShape10,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = shape)
                    .border(
                            width = MaterialTheme.spacing.spaceSingleDp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = shape
                    )
                    .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceTen)
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.BodySmallRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (onClick != null) {
                Text(
                        text = value,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = MaterialTheme.typography.labelLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = keyboardOptions,
                        modifier = Modifier
                                .weight(1f)
                                .padding(vertical = MaterialTheme.spacing.spaceSmall)
                )
            }

            if (isDropDown) {
                Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepsEditorPreview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {

            StepsEditor(
                   uiState = HomeUiState(),
                    onEvent = {}
            )
        }
    }
}
