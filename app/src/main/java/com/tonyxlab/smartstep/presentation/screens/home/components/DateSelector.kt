package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.StandardWheelPicker
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape28
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelector(
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { onEvent(HomeUiEvent.DismissDateSelector) }) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.RoundedCornerShape28),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.spaceMedium)
                    .padding(top = MaterialTheme.spacing.spaceSmall)
            ) {
                Text(
                    text = stringResource(id = R.string.header_text_date),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceMedium)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val maxDays = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
                    val days = remember(selectedMonth, selectedYear) {
                        (1..maxDays).toList() }

                 //   val days = remember { (1..31).toList() }
                    val years = remember { (2020..2030).toList() }
                    val months = remember { (1..12).toList() }

                    StandardWheelPicker(
                            modifier = Modifier.weight(1f),
                            selectedValue = selectedDay,
                            values = days,
                            onValueSelected = { onEvent(HomeUiEvent.OnDaySelected(it)) },
                            labelFormatter = { it.toString().padStart(2, '0') }
                    )

                    StandardWheelPicker(
                        modifier = Modifier.weight(1f),
                        selectedValue = selectedMonth,
                        values = months,
                        onValueSelected = { onEvent(HomeUiEvent.OnMonthSelected(it)) },
                        labelFormatter = { it.toString().padStart(2, '0') }
                    )

                    StandardWheelPicker(
                            modifier = Modifier.weight(1f),
                            selectedValue = selectedYear,
                            values = years,
                            onValueSelected = { onEvent(HomeUiEvent.OnYearSelected(it)) },
                            labelFormatter = { it.toString() }
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onEvent(HomeUiEvent.DismissDateSelector) }) {
                        Text(
                            text = stringResource(id = R.string.button_text_cancel),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceMedium))
                    TextButton(onClick = { onEvent(HomeUiEvent.ConfirmDateSelection) }) {
                        Text(
                            text = stringResource(id = R.string.button_text_save),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateSelectorPreview() {
    SmartStepTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            DateSelector(
                selectedYear = 2025,
                selectedMonth = 11,
                selectedDay = 30,
                onEvent = {}
            )
        }
    }
}
