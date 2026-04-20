@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape24
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun AiInsightCard(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    onTryAgainClick: () -> Unit = {}
) {
    Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.RoundedCornerShape24,
            colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
            )

    ) {
        Column(
                modifier = Modifier
                        .padding(MaterialTheme.spacing.spaceMedium)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
            ) {
                Box(
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceDoubleDp * 19)
                                .background(
                                        color = MaterialTheme.colorScheme.secondary,
                                        shape = MaterialTheme.shapes.small
                                ),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            modifier = Modifier.size(MaterialTheme.spacing.spaceTen * 2),
                            painter = painterResource(id = R.drawable.ic_ai),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                        modifier = Modifier
                                .clickable(onClick = onTryAgainClick)
                                .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                                .padding(vertical = MaterialTheme.spacing.spaceTen),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.insightMessageState.isOnline) {
                        Text(
                                text = stringResource(id = R.string.text_button_more),
                                style = MaterialTheme.typography.BodyLargeMedium,
                                color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                                modifier = Modifier.size(MaterialTheme.spacing.spaceTwelve * 2),
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                                text = stringResource(id = R.string.text_button_try_again),
                                style = MaterialTheme.typography.BodyLargeMedium,
                                color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                                modifier = Modifier.size(MaterialTheme.spacing.spaceTwelve * 2),
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            with(uiState.insightMessageState) {
                if (this.isInsightLoading) {
                    InsightLoadingContent()
                } else {
                    Text(
                            text = if (isOnline)
                                insightMessage
                            else stringResource(id = R.string.caption_text_connect_internet),
                            style = MaterialTheme.typography.BodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun InsightLoadingContent(modifier: Modifier = Modifier) {

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

@Preview
@Composable
private fun AiInsightCardPreview() {

    val state1 = HomeUiState().run {
        copy(insightMessageState = this.insightMessageState.copy(isOnline = false))
    }

    val state2 = HomeUiState().run {
        copy(insightMessageState = this.insightMessageState.copy(isOnline = true))
    }

    SmartStepTheme {
        SmartStepTheme {
            Column(
                    modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .padding(MaterialTheme.spacing.spaceMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
            ) {
                AiInsightCard(
                        uiState = state1,
                        onEvent = {}
                )
                AiInsightCard(
                        uiState = state2,
                        onEvent = {}
                )
            }
        }
    }
}
