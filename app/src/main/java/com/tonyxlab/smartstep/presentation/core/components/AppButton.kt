package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BackgroundWhite20
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun AppButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonShape: Shape = MaterialTheme.shapes.RoundedCornerShape10,
    buttonHeight: Dp = MaterialTheme.spacing.spaceDoubleDp * 22,
) {

    val backgroundModifier = when {
        enabled -> {
            Modifier
                    .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = buttonShape
                    )
        }

        else ->
            Modifier.background(
                    color = BackgroundWhite20,
                    shape = buttonShape
            )
    }
    Box(
            modifier = modifier
                    .clip(shape = buttonShape)
                    .then(backgroundModifier)
                    .height(buttonHeight)
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .padding(
                            horizontal = MaterialTheme.spacing.spaceTwelve * 2,
                            vertical = MaterialTheme.spacing.spaceTen
                    ),
            contentAlignment = Alignment.Center) {

        Text(
                modifier = Modifier.animateContentSize(
                        animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessHigh
                        )
                ),
                text = buttonText,
                style = MaterialTheme.typography.BodyLargeMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@PreviewLightDark
@Composable
private fun AppButton_Preview() {
    SmartStepTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppButton(
                    buttonText = stringResource(R.string.start),
                    onClick = {}
            )

        }
    }
}
