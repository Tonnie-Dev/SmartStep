package com.tonyxlab.smartstep.presentation.screens.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun PickerContainerAi(
    modifier: Modifier = Modifier,
    pickerTitle: String,
    pickerDescription: String,
    unitOneText: String,
    unitTwoText: String,
    onSelectMeasurementMode: () -> Unit,
    onCancel: () -> Unit,
    wheelPicker: @Composable (() -> Unit)? = null,
    onConfirm: (Int, Int, Boolean) -> Unit = { _, _, _ -> }

) {
    var isCmSelected by remember { mutableStateOf(false) }
    var selectedFeet by remember { mutableIntStateOf(5) }
    var selectedInches by remember { mutableIntStateOf(9) }
    var selectedCm by remember { mutableIntStateOf(175) }

    val feetItems = remember { (0..8).map { it.toString() } }
    val inchItems = remember { (0..11).map { it.toString() } }
    val cmItems = remember { (100..250).map { it.toString() } }
}

/*
                           TextButton(onClick = {
                               if (isCmSelected) {
                                   onConfirm(selectedCm, 0, true)
                               } else {
                                   onConfirm(selectedFeet, selectedInches, false)
                               }
                           }) {
                               Text(
                                       text = stringResource(R.string.button_text_ok),
                                       style = MaterialTheme.typography.BodyLargeMedium.copy(fontSize = 18.sp),
                                       color = MaterialTheme.colorScheme.primary
                               )
                           }
           */
@Composable
fun SomeComposable(modifier: Modifier = Modifier) {

    /*    Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(28.dp)
                        )
                        .padding(4.dp)
        ) {
            Box(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (isCmSelected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                            .clickable { isCmSelected = true },
                    contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isCmSelected) {
                        Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                            text = stringResource(R.string.label_text_cm),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Box(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (!isCmSelected) MaterialTheme.colorScheme.secondary else Color.Transparent)
                            .clickable { isCmSelected = false },
                    contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isCmSelected) {
                        Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                            text = stringResource(R.string.label_text_ft_in),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))

        // Wheel Pickers
        Box(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                contentAlignment = Alignment.Center
        ) {
            // Background for selected row
            Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            )

            if (isCmSelected) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                ) {
                    WheelPicker(
                            items = cmItems,
                            initialIndex = cmItems.indexOf(selectedCm.toString())
                                    .coerceAtLeast(0),
                            onItemSelected = { selectedCm = cmItems[it].toInt() },
                            modifier = Modifier.width(100.dp),
                            visibleItemsCount = 5
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                            text = stringResource(R.string.label_text_cm),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                ) {
                    WheelPicker(
                            items = feetItems,
                            initialIndex = feetItems.indexOf(selectedFeet.toString())
                                    .coerceAtLeast(0),
                            onItemSelected = { selectedFeet = feetItems[it].toInt() },
                            modifier = Modifier.width(60.dp),
                            visibleItemsCount = 5
                    )
                    Text(
                            text = stringResource(R.string.label_text_ft),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                    WheelPicker(
                            items = inchItems,
                            initialIndex = inchItems.indexOf(selectedInches.toString())
                                    .coerceAtLeast(0),
                            onItemSelected = { selectedInches = inchItems[it].toInt() },
                            modifier = Modifier.width(60.dp),
                            visibleItemsCount = 5
                    )
                    Text(
                            text = stringResource(R.string.label_text_in),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))
    */
}

