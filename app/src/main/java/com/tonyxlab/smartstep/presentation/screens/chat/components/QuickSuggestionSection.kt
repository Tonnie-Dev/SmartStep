package com.tonyxlab.smartstep.presentation.screens.chat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyMediumMedium
import com.tonyxlab.smartstep.presentation.theme.TextPrimary

@Composable
fun QuickSuggestionsSection(
    expanded: Boolean,
    onToggleSuggestionsDrawer: () -> Unit,
    onSelectPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val suggestions = remember {
        listOf(
                R.string.label_step_suggestion_1,
                R.string.label_step_suggestion_2,
                R.string.label_step_suggestion_3
        )
    }

    Column(
            modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
    ) {

        Column(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
        ) {
            Row(
                    modifier = Modifier.clickable { onToggleSuggestionsDrawer() },
                    verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                        text = stringResource(R.string.label_step_quick_suggestions),
                        style = MaterialTheme.typography.BodyMediumMedium,
                        color = TextPrimary
                )

                Icon(
                        modifier = Modifier
                                .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                                .padding(vertical = MaterialTheme.spacing.spaceTen),
                        painter = painterResource(
                                id = if (expanded)
                                    R.drawable.ic_chevron_down
                                else
                                    R.drawable.ic_chevron_up
                        ),
                        contentDescription = null,
                        tint = TextPrimary
                )
            }

            AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(
                            animationSpec = tween(250),
                            expandFrom = Alignment.Top
                    ) + fadeIn(animationSpec = tween(250)),
                    exit = shrinkVertically(
                            animationSpec = tween(200),
                            shrinkTowards = Alignment.Top
                    ) + fadeOut(animationSpec = tween(200))
            ) {
                Column(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceMedium),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                ) {
                    suggestions.fastForEach {
                        SuggestionItem(
                                text = stringResource(it),
                                onSelectPromptItem = onSelectPrompt
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    text: String,
    modifier: Modifier = Modifier,
    onSelectPromptItem: (String) -> Unit
) {
    Box(
            modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                            width = MaterialTheme.spacing.spaceSingleDp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .clickable {
                        onSelectPromptItem(text)
                    }
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceTwelve)
    ) {
        Text(
                text = text,
                style = MaterialTheme.typography.BodyMediumMedium,
                color = TextPrimary
        )
    }
}

