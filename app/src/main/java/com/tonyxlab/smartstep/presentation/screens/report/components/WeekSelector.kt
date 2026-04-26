@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.ifThen

@Composable
fun WeekSelector(
    uiState: ReportUiState,
    onEvent: (ReportUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
    ) {
        Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceLarge)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .clickable { onEvent(ReportUiEvent.ViewPreviousWeek) },
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface
                )
            }

            Text(
                 modifier = Modifier.width(MaterialTheme.spacing.spaceOneHundredFifty),
                    text = uiState.weekDateState.currentWeekRange,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1
            )

            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceLarge)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.primary)
                            .ifThen(uiState.weekDateState.isCurrentWeek) {
                                background(color = MaterialTheme.colorScheme.secondary)
                            }
                            .clickable(enabled = !uiState.weekDateState.isCurrentWeek) {
                                onEvent(ReportUiEvent.ViewNextWeek)
                            },
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekSelector_Preview() {
    SmartStepTheme {

        Column(modifier = Modifier.fillMaxSize()) {

            WeekSelector(
                   uiState = ReportUiState(),
                    onEvent = {}
            )
        }
    }
}
