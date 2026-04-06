@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.AnalyticsHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.DayStats
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.BackgroundWhite
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.ProgressGreen
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape24
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.formatWithCommas

@Composable
fun WeeklyAnalyticsSection(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.RoundedCornerShape24)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(MaterialTheme.spacing.spaceMedium)
    ) {
        Text(
                text = stringResource(
                        id = R.string.label_daily_average,
                        uiState.weeklyAnalyticState.dailyAverageSteps.formatWithCommas()
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            uiState.weeklyAnalyticState.weeklyStats.forEach { stats ->
                DayProgressItem(
                        stats = stats,
                        modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun DayProgressItem(
    stats: DayStats,
    modifier: Modifier = Modifier
) {

    Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceExtraSmall)
    ) {
        Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 5.dp.toPx()
                drawCircle(
                        color = BackgroundWhite,
                        style = Stroke(width = strokeWidth)
                )
                drawArc(
                        color = ProgressGreen,
                        startAngle = -90f,
                        sweepAngle = 360f * stats.progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth / 2, cap = StrokeCap.Round)
                )
            }
        }

        Text(
                text = stats.dayLabel,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
        )

        Text(
                text = stats.steps.formatWithCommas(),
                style = MaterialTheme.typography.BodySmallRegular,
                color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun WeeklyAnalyticsSectionPreview() {

    val analyticsHandler = AnalyticsHandler()

    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            WeeklyAnalyticsSection(
                    uiState = HomeUiState()
            )
        }
    }
}
