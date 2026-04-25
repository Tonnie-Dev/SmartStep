package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.tonyxlab.smartstep.presentation.core.utils.spacing

@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition(label = "insight_loading")

    val alpha by infiniteTransition.animateFloat(
            initialValue = .3f,
            targetValue = .8f,
            animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 700),
                    repeatMode = RepeatMode.Reverse
            ),
            label = "insight_loading_alpha"
    )

    Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTen)
    ) {

        Box(
                modifier = Modifier
                        .fillMaxWidth(.9f)
                        .height(MaterialTheme.spacing.spaceTen * 2)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                                color = MaterialTheme.colorScheme.onSurface
                                        .copy(alpha = alpha * .18f)
                        )
        )

        Box(
                modifier = Modifier
                        .fillMaxWidth(.7f)
                        .height(MaterialTheme.spacing.spaceTen * 2)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                                color = MaterialTheme.colorScheme.onSurface
                                        .copy(alpha = alpha * .18f)
                        )
        )

        Box(
                modifier = Modifier
                        .fillMaxWidth(.5f)
                        .height(MaterialTheme.spacing.spaceTen * 2)
                        .clip(MaterialTheme.shapes.small)
                        .background(
                                color = MaterialTheme.colorScheme.onSurface
                                        .copy(alpha = alpha * .18f)
                        )
        )
    }
}

