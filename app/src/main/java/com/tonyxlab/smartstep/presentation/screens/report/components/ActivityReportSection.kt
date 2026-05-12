@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.screens.report.model.ActivityState
import com.tonyxlab.smartstep.presentation.screens.report.model.ActivityUiItem
import com.tonyxlab.smartstep.presentation.screens.report.model.MetricType
import com.tonyxlab.smartstep.presentation.screens.report.model.toActivityState
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.borderStroke
import com.tonyxlab.smartstep.utils.ifThen
import com.tonyxlab.smartstep.utils.toDayName

@Composable
fun ActivityReportSection(
    uiState: ReportUiState,
    modifier: Modifier = Modifier,
    isDeviceWide: Boolean = false
) {
    val items = uiState.activityReportState.weeklyMetrics
    val metricType = uiState.activityReportState.selectedMetricType

    if (isDeviceWide){

        LazyVerticalGrid(
                modifier = modifier
                        .padding(bottom = MaterialTheme.spacing.spaceTen * 5),
                columns = GridCells.Fixed(count = 2),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
        ) {
            items(
                    items = items,
                    key = { it.date }
            ) { metric ->

                ActivityItem(
                        modifier = Modifier,
                        activityUiItem = ActivityUiItem(
                                dayName = metric.date.toDayName(),
                                metricValue = metricType.getDisplayValue(metric),
                                metricType = metricType,
                                stepGoal = metric.dailyStepGoal,
                                activityState = metric.toActivityState(metricType)
                        )
                )
            }
        }
    }else {

        LazyColumn(
                modifier = modifier
                        .padding(bottom = MaterialTheme.spacing.spaceTen * 5),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
        ) {

            items(
                    items = items,
                    key = { it.date }
            ) { metric ->

                ActivityItem(
                        modifier = Modifier,
                        activityUiItem = ActivityUiItem(
                                dayName = metric.date.toDayName(),
                                metricValue = metricType.getDisplayValue(metric),
                                metricType = metricType,
                                stepGoal = metric.dailyStepGoal,
                                activityState = metric.toActivityState(metricType)
                        )
                )
            }
        }
    }
}

@Composable
fun ActivityItem(
    activityUiItem: ActivityUiItem,
    modifier: Modifier = Modifier
) {

    val activityState = activityUiItem.activityState
    val isToday = activityState == ActivityState.IN_PROGRESS

    Column(
            modifier = modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .ifThen(isToday.not()) {
                        border(border = borderStroke(), shape = MaterialTheme.shapes.medium)
                    }
                    .ifThen(isToday) {
                        border(
                                border = borderStroke(outlineColor = MaterialTheme.colorScheme.primary),
                                shape = MaterialTheme.shapes.medium
                        )
                    }
                    .padding(MaterialTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)

    ) {

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                    text = activityUiItem.dayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = W600
                    ),
                    color = when(activityUiItem.activityState){
                        ActivityState.HAS_DATA ->MaterialTheme.colorScheme.onSecondary
                        ActivityState.IN_PROGRESS ->MaterialTheme.colorScheme.primary
                        ActivityState.NO_DATA ->MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    /*color = if (isToday)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant*/
            )

            Box(
                    modifier = Modifier
                            .clip(shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.secondary)
                            .ifThen(activityState == ActivityState.NO_DATA) {
                                background(color = MaterialTheme.colorScheme.tertiary)
                            }
                            .size(MaterialTheme.spacing.spaceTwelve * 2),
                    contentAlignment = Alignment.Center) {
                Icon(
                        painter = painterResource(
                                id = when (activityState) {
                                    ActivityState.HAS_DATA -> R.drawable.ic_checkmark
                                    ActivityState.IN_PROGRESS -> R.drawable.ic_clock_2
                                    ActivityState.NO_DATA -> R.drawable.ic_minus
                                }
                        ),
                        contentDescription = null
                )
            }
        }

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceExtraSmall),
                    verticalAlignment = Alignment.Bottom

            ) {
                Text(
                        text = activityUiItem.metricValue,
                        style = MaterialTheme.typography.BodyLargeMedium,
                        color = if (activityState == ActivityState.IN_PROGRESS)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                        text = activityUiItem.metricType.unitName(),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            if (activityUiItem.metricType == MetricType.STEPS
                || activityState == ActivityState.NO_DATA
            ) {

                if (activityState == ActivityState.NO_DATA) {

                    Text(
                            text = stringResource(id = R.string.label_text_no_data),
                            style = MaterialTheme.typography.BodySmallRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                } else {

                    Text(
                            text = stringResource(
                                    id = R.string.label_text_step_goal,
                                    activityUiItem.stepGoal
                            ),
                            style = MaterialTheme.typography.BodySmallRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActivityItem_Preview() {
    SmartStepTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            ActivityReportSection(uiState = ReportUiState())
        }
    }
}
