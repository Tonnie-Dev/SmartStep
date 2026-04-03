package com.tonyxlab.smartstep.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.components.SelectionField
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectionField(
    label: String,
    selectedGender: Gender,
    options: List<Gender>,
    modifier: Modifier = Modifier,
    onSelectOption: (Gender) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
    ) {

        SelectionField(
                modifier = Modifier
                        .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                        )

                        .fillMaxWidth(),
                value = selectedGender.toString(),
                label = label,
                onClick = { }
        )
        DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                        .exposedDropdownSize() // Critical: Forces menu to exactly match the anchor's width
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = MaterialTheme.spacing.spaceSmall),
                shape = RoundedCornerShape(8.dp),
                offset = DpOffset(
                        x = MaterialTheme.spacing.spaceDefault,
                        y = MaterialTheme.spacing.spaceSmall
                )
        )
        {

            options.fastForEach { gender ->

                val isSelected = gender == selectedGender

                DropdownMenuItem(
                        modifier = Modifier
                                .background(
                                        color = if (isSelected)
                                            MaterialTheme.colorScheme.surfaceVariant
                                        else
                                            Color.Transparent,
                                        shape = MaterialTheme.shapes.RoundedCornerShape10
                                )
                                .fillMaxWidth()

                                .padding(vertical = MaterialTheme.spacing.spaceDoubleDp * 3),
                        text = {
                            Row(
                                    modifier = Modifier
                                            .fillMaxWidth(),

                                    horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                        text = gender.toString(),
                                        style = MaterialTheme.typography.bodyLarge
                                )

                                if (isSelected) {
                                    Icon(
                                            painter = painterResource(R.drawable.ic_check),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
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
                    onSelectOption = {},
            )

            GenderSelectionField(
                    label = stringResource(id = R.string.label_text_gender),
                    options = options,
                    selectedGender = Gender.FEMALE,
                    onSelectOption = {},
            )
        }
    }
}
