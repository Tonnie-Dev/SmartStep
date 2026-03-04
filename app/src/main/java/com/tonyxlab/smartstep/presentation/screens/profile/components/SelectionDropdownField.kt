package com.tonyxlab.smartstep.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionDropdownField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onSelectOption: (String) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {

    ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {}
    ) {

        OutlinedTextField(
                modifier = Modifier
                        // TODO: Check this Deprecation
                        .menuAnchor()
                        .fillMaxWidth(),
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                            text = label,
                            style = MaterialTheme.typography.BodySmallRegular.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),

                            )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )

        ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
        ) {

            options.fastForEach { option ->
                DropdownMenuItem(

                        text = {
                            Text(
                                    text = option,
                                    style = MaterialTheme.typography.BodyLargeRegular.copy(
                                            color = MaterialTheme.colorScheme.onSurface
                                    )
                            )
                        },
                        onClick = { onSelectOption(option) }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SelectionDropdownField_Preview() {

    val options = listOf("Male", "Female")

    SmartStepTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {

            SelectionDropdownField(
                    label = stringResource(id = R.string.label_text_gender),
                    options = options,
                    selectedOption = "Male",
                    onSelectOption = {},
                    expanded = false
            )

            SelectionDropdownField(
                    label = stringResource(id = R.string.label_text_gender),
                    options = options,
                    selectedOption = "Male",
                    onSelectOption = {},
                    expanded = true
            )
        }
    }
}