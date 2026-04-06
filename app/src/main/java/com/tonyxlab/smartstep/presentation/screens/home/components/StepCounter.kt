@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.BackgroundWhite20
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape24
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape4
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.TittleAccent
import com.tonyxlab.smartstep.utils.MetadataValue
import com.tonyxlab.smartstep.utils.formatWithCommas
import com.tonyxlab.smartstep.utils.metadataAnnotatedString

@Composable
fun StepCounterCard(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val stepsGoal = uiState.stepGoalSheetState.selectedStepsGoal
    val currentSteps = uiState.currentSteps
    val progress = if (stepsGoal > 0) currentSteps.toFloat() / stepsGoal else 0f

    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.RoundedCornerShape24)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(all = MaterialTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTen * 4)
                            .clip(shape = MaterialTheme.shapes.small)
                            .background(BackgroundWhite20),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_sneakers),
                        contentDescription = stringResource(id = R.string.cds_shoe_icon),
                        tint = Color.White
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)) {
                Box(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceTen * 4)
                                .clip(shape = CircleShape)
                                .background(BackgroundWhite20)
                                .clickable { onEvent(HomeUiEvent.EditSteps) },
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = stringResource(id = R.string.cds_shoe_icon),
                            tint = Color.White
                    )
                }
                Box(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceTen * 4)
                                .clip(shape = CircleShape)
                                .background(BackgroundWhite20)
                                .clickable { onEvent(HomeUiEvent.PauseStepCounting) },
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            modifier = Modifier,
                            painter = if (uiState.stepEditorState.paused) painterResource(id = R.drawable.ic_play) else painterResource(
                                    R.drawable.ic_pause
                            ),
                            contentDescription = stringResource(id = R.string.cds_shoe_icon),
                            tint = Color.White
                    )
                }
            }
        }

        Column {
            Text(
                    text = currentSteps.formatWithCommas(),
                    style = MaterialTheme.typography.TittleAccent,
                    color = if (uiState.stepEditorState.paused) BackgroundWhite20 else MaterialTheme.colorScheme.surface,
            )

            Text(
                    text = if (uiState.stepEditorState.paused)
                        stringResource(id = R.string.caption_text_paused)
                    else
                        stringResource(
                                id = R.string.template_steps_goal,
                                stepsGoal,
                                stringResource(id = R.string.label_steps)
                        ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.background
            )
        }

        CustomProgressBar(
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceLarge),
                progress = progress,
        )

        with(uiState.metricDataState) {
            MetricSection(
                    modifier = Modifier,
                    distanceTraveled = distance,
                    caloriesBurnt = calories,
                    time = time
            )
        }
    }
}

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .clip(MaterialTheme.shapes.RoundedCornerShape4)
                    .background(BackgroundWhite20)
                    .fillMaxWidth()
    ) {
        Box(
                modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(MaterialTheme.spacing.spaceSmall)
                        .clip(MaterialTheme.shapes.RoundedCornerShape4)
                        .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
private fun MetricSection(
    distanceTraveled: Double,
    caloriesBurnt: Int,
    time: Int,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
    ) {
        MetadataItem(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_pin,
                value = MetadataValue.Decimal(value = distanceTraveled),
                unit = "km",
        )
        MetadataItem(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_weight,
                value = MetadataValue.WholeNumber(value = caloriesBurnt),
                unit = "kcal",
        )
        MetadataItem(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.ic_clock,
                value = MetadataValue.WholeNumber(value = time),
                unit = "min",
        )
    }

}

@Composable
private fun MetadataItem(
    @DrawableRes
    iconRes: Int,
    value: MetadataValue,
    unit: String,
    modifier: Modifier = Modifier,
    metadataValueStyle: TextStyle = MaterialTheme.typography.titleMedium,
    metadataUnitStyle: TextStyle = MaterialTheme.typography.labelMedium
) {

    Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
                modifier = Modifier
                        .size(MaterialTheme.spacing.spaceDoubleDp * 22)
                        .clip(shape = MaterialTheme.shapes.small)
                        .background(BackgroundWhite20),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    modifier = Modifier,
                    painter = painterResource(iconRes),
                    contentDescription = stringResource(id = R.string.cds_shoe_icon),
                    tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTen))
        Text(

                text = metadataAnnotatedString(
                        metadataValue = value,
                        unit = unit,
                        metadataValueStyle = metadataValueStyle.copy(color = MaterialTheme.colorScheme.surface),
                        unitStyle = metadataUnitStyle.copy(color = MaterialTheme.colorScheme.surfaceVariant)
                )
        )
    }

}

@PreviewLightDark
@Composable
private fun MetadataItem_Preview() {

    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
            ) {
                MetadataItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_pin,
                        value = MetadataValue.Decimal(3.9),
                        unit = "km",

                        )
                MetadataItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_weight,
                        value = MetadataValue.WholeNumber(12),
                        unit = "kcal",
                )
                MetadataItem(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.ic_clock,
                        value = MetadataValue.WholeNumber(12),
                        unit = "min",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StepCounterCard_Preview() {
    val state = HomeUiState().let {
        it.copy(
                currentSteps = 1313,
                stepEditorState = it.stepEditorState.copy(paused = false)

        )
    }
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            StepCounterCard(
                    uiState = state,
                    onEvent = {}
            )
        }
    }
}
