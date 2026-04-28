@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.report.handling.MetricType
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.theme.BodyMediumMedium
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.borderStroke

@Composable
fun BottomMetricTabs(
    uiState: ReportUiState,
    onEvent:(ReportUiEvent)-> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .border(
                            border = borderStroke(),
                            shape = MaterialTheme.shapes.medium
                    ),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
        ) {
            MetricType.entries.forEachIndexed { index, metricType ->

                MetricTabItem(
                        metricType = metricType,
                        isSelected = uiState.selectedMetricType == metricType,
                        onClick = { onEvent(ReportUiEvent.SelectMetricType(metricType )) },
                        modifier = Modifier.weight(1f)
                )

                if (index < MetricType.entries.size - 1) {
                    VerticalDivider(
                            modifier = Modifier
                                    .fillMaxHeight()
                                    .width(MaterialTheme.spacing.spaceSingleDp),
                            color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricTabItem(
    metricType: MetricType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val iconRes = when (metricType) {
        MetricType.STEPS -> R.drawable.ic_sneakers
        MetricType.CALORIES -> R.drawable.ic_weight
        MetricType.TIME -> R.drawable.ic_clock
        MetricType.DISTANCE -> R.drawable.ic_pin
    }

    val labelRes = when (metricType) {
        MetricType.STEPS -> R.string.label_text_steps
        MetricType.CALORIES -> R.string.label_text_calories
        MetricType.TIME -> R.string.label_text_time
        MetricType.DISTANCE -> R.string.label_text_distance
    }

    Column(
            modifier = modifier
                    .background(backgroundColor)
                    .clickable(onClick = onClick)
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Icon(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(id = labelRes),
                tint = contentColor,

        )
        Text(
                text = stringResource(id = labelRes),
                style = MaterialTheme.typography.BodyMediumMedium,
                color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomMetricTabsPreview() {
    SmartStepTheme {
        Column(modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.spaceMedium)) {
            BottomMetricTabs(
                    uiState = ReportUiState(),
                    onEvent = {}
            )
        }
    }
}
