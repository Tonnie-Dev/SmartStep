package com.tonyxlab.smartstep.presentation.screens.report.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.report.handling.MetricType
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyMediumRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.TittleAccent
import com.tonyxlab.smartstep.utils.formatWithCommas

@Composable
fun SummaryCard(
    uiState: ReportUiState,
    modifier: Modifier = Modifier
) {

    Column(
            modifier = modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .padding(all = MaterialTheme.spacing.spaceExtraSmall * 6)
    ) {

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                    text = when(uiState.selectedMetricType) {
                       MetricType.STEPS -> stringResource(id = R.string.label_steps) 
                       MetricType.CALORIES -> stringResource(id = R.string.label_text_calories)
                       MetricType.TIME -> stringResource(id = R.string.label_text_minutes)
                       else -> stringResource(id = R.string.label_text_kilometers)
                    },
                    style = MaterialTheme.typography.BodyLargeMedium,
                    color = MaterialTheme.colorScheme.surface,
            )
            Text(
                    text = stringResource(id = R.string.label_text_this_week),
                    style = MaterialTheme.typography.BodyLargeMedium,
                    color = MaterialTheme.colorScheme.surface,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)) {

            Text(
                    text = uiState.stepCount.formatWithCommas(),
                    style = MaterialTheme.typography.TittleAccent,
                    color = MaterialTheme.colorScheme.surface,
            )

            Text(
                    text = stringResource(
                            id = R.string.label_text_daily_average,
                            uiState.averageSteps
                    ),
                    style = MaterialTheme.typography.BodyMediumRegular,
                    color = MaterialTheme.colorScheme.background,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryCard_Preview() {

    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {

            SummaryCard(uiState = ReportUiState())

        }
    }
}
