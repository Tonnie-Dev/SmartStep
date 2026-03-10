package com.tonyxlab.smartstep.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.profile.handling.Gender
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionField(
    label: String,
    selectedGender: Gender,
    options: List<Gender>,
    modifier: Modifier = Modifier,
    onClickGenderSelection: () -> Unit,
    onSelectOption: (Gender) -> Unit

) {

    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = { expanded != expanded }
    ) {

        ProfileSelectionField(
                // TODO: Check on this deprecation
                modifier = Modifier.menuAnchor()
                        .fillMaxWidth(),
                value = selectedGender.toString(),
                label = label,
                onClick = { onClickGenderSelection()}
        )
        /*  OutlinedTextField(
                  modifier = Modifier

                          .menuAnchor()
                          .fillMaxWidth(),
                  value = selectedGender.toString(),
                  onValueChange = {},
                  readOnly = true,
                  label = {
                      Text(
                              text = label,
                              style = MaterialTheme.typography.BodySmallRegular.copy(
                                      color = MaterialTheme.colorScheme.onSurfaceVariant
                              )
                      )
                  },
                  trailingIcon = {
                      ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                  },
                  colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
          )*/

        ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
        ) {

            options.fastForEach { gender ->

                DropdownMenuItem(
                        text = {
                            Text(
                                    text = gender.toString(),
                                    style = MaterialTheme.typography.BodyLargeRegular.copy(
                                            color = MaterialTheme.colorScheme.onSurface
                                    )
                            )
                        },
                        onClick = {
                            onSelectOption(gender)
                            expanded = false
                        }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun GenderSelectionField_Preview() {

    val options = Gender.entries

    SmartStepTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {

            GenderSelectionField(
                    label = stringResource(id = R.string.label_text_gender),
                    options = options,
                    selectedGender = Gender.MALE,
                    onClickGenderSelection = {},
                    onSelectOption = {},

                    )

            GenderSelectionField(
                    label = stringResource(id = R.string.label_text_gender),
                    options = options,
                    selectedGender = Gender.FEMALE,
                    onClickGenderSelection = {},
                    onSelectOption = {},

                    )
        }
    }
}