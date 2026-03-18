package com.tonyxlab.smartstep.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BackgroundWhite20
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape24
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape4
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.TittleAccent
import com.tonyxlab.smartstep.utils.formatWithCommas

@Composable
fun StepCounterCard(
    currentSteps: Int,
    stepsGoal: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (stepsGoal > 0) currentSteps.toFloat() / stepsGoal else 0f

    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.RoundedCornerShape24)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(all = MaterialTheme.spacing.spaceMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
    ) {
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

        Column {
            Text(
                    text = currentSteps.formatWithCommas(),
                    style = MaterialTheme.typography.TittleAccent,
                    color = MaterialTheme.colorScheme.surface,
            )

            Text(
                    text = stringResource(
                            id = R.string.template_steps_goal,
                            stepsGoal,
                            stringResource(id = R.string.label_steps)
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.surface
            )
        }

        CustomProgressBar(
                modifier = Modifier,
                progress = progress,
        )
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
                        .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StepCounterCardPreview() {
    SmartStepTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            StepCounterCard(
                    currentSteps = 4523,
                    stepsGoal = 6000
            )
        }
    }
}
