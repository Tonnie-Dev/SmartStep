package com.tonyxlab.smartstep.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape24
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun AiInsightCard(
    offline: Boolean,
    modifier: Modifier = Modifier,
    aiPrompt: String = "You are slightly behind",
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
                    if (offline) {
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
                    } else {

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

                    }
                }
            }

            Text(
                    text = if (offline) stringResource(id = R.string.caption_text_connect_internet) else aiPrompt,
                    style = MaterialTheme.typography.BodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview
@Composable
private fun AiInsightCardPreview() {
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
                AiInsightCard(offline = true)
                AiInsightCard(offline = false)
            }
        }
    }

}
