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
import com.tonyxlab.smartstep.presentation.screens.report.handling.MetricType
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodySmallRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.borderStroke
import com.tonyxlab.smartstep.utils.ifThen

@Composable
fun ActivityItem(
    metricValue: Int,
    day: String,
    stepGoal: Int,
    hasData: Boolean,
    isToday: Boolean,
    metricType: MetricType,
    modifier: Modifier = Modifier
) {

    Column(
            modifier = modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .border(border = borderStroke(), shape = MaterialTheme.shapes.medium)
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
                    text = day,
                    style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = W600
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                    modifier = Modifier
                            .clip(shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.secondary)
                            .ifThen(!hasData) {
                                background(color = MaterialTheme.colorScheme.tertiary)
                            }
                            .size(MaterialTheme.spacing.spaceTwelve * 2),
                    contentAlignment = Alignment.Center) {
                Icon(

                        painter = painterResource(
                                id = when {
                                    hasData && !isToday -> R.drawable.ic_checkmark
                                    isToday && hasData -> R.drawable.ic_clock
                                    else -> R.drawable.ic_minus
                                }
                        ),
                        contentDescription = null,

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
                        text = stringResource(
                                id = R.string.label_text_metric_value,
                                metricValue
                        ),
                        style = MaterialTheme.typography.BodyLargeMedium,
                        color = if (isToday)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                        text = stringResource(
                                id = R.string.label_text_activity_type,
                                metricType.toString()
                        ),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            if (metricType == MetricType.STEPS) {
                Text(

                        text = stringResource(
                                id = R.string.label_text_step_goal,
                                stepGoal
                        ),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (hasData.not()) {
                Text(
                        text = stringResource(id = R.string.label_text_no_data),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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

            ActivityItem(
                    metricValue = 300,
                    day = "Monday",
                    stepGoal = 3000,
                    hasData = true,
                    isToday = false,
                    metricType = MetricType.STEPS,
            )

            ActivityItem(
                    metricValue = 300,
                    day = "Thursday",
                    stepGoal = 3000,
                    hasData = true,
                    isToday = true,
                    metricType = MetricType.STEPS,
            )

            ActivityItem(
                    metricValue = 300,
                    day = "Thursday",
                    stepGoal = 3000,
                    hasData = false,
                    isToday = false,
                    metricType = MetricType.STEPS,
            )


        }
    }
}
