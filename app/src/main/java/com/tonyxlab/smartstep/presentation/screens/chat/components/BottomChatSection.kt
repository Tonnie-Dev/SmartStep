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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.components.AppInputField
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiState
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.BodyMediumMedium
import com.tonyxlab.smartstep.presentation.theme.ButtonPrimary
import com.tonyxlab.smartstep.presentation.theme.ButtonSecondary
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.TextPrimary
import com.tonyxlab.smartstep.presentation.theme.TextWhite
import com.tonyxlab.smartstep.utils.borderStroke
import com.tonyxlab.smartstep.utils.ifThen
import com.tonyxlab.smartstep.utils.rememberIsDeviceWide

@Composable
fun BottomChatSection(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDeviceWide = rememberIsDeviceWide()

    Column(
            modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
    ) {

        Column(
                modifier = Modifier
                        .ifThen(isDeviceWide) {
                            Modifier.width(400.dp)
                        }
        ) {

            QuickSuggestionsSection(
                    modifier = Modifier,
                    expanded = uiState.suggestionsExpanded,
                    onToggleSuggestionsDrawer = { onEvent(ChatUiEvent.ToggleSuggestionsDrawer) },
                    onSelectPrompt = { onEvent(ChatUiEvent.SelectPrompt((it))) }
            )

            ChatInputSection(
                    modifier = Modifier,
                    uiState = uiState,
                    onEvent = onEvent
            )
        }
    }
}

@Composable
private fun QuickSuggestionsSection(
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


    Column(modifier = modifier.fillMaxWidth()) {

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

@Composable
private fun ChatInputSection(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val canSend = uiState.isOnline &&
            uiState.textFieldState.text.toString()
                    .trim()
                    .isNotEmpty()

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
    ) {

        if (uiState.isOnline) {

            Column(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
            ) {
                AppInputField(
                        modifier = Modifier,
                        textFieldState = uiState.textFieldState,
                        height = MaterialTheme.spacing.spaceLarge,
                        placeholderText = stringResource(id = R.string.label_step_ask_anything),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
                        isChatInputField = true
                )
            }

            SendButton(
                    enabled = canSend,
                    onSend = {
                        if (!canSend) return@SendButton
                        onEvent(ChatUiEvent.SendChatMessage)
                        focusManager.clearFocus(force = true)
                        keyboardController?.hide()
                    }
            )
        } else {
            Box(
                    modifier = Modifier
                            .weight(1f)
                            .clip(shape = MaterialTheme.shapes.RoundedCornerShape10)
                            .border(border = borderStroke())
                            .padding(all = MaterialTheme.spacing.spaceMedium)

            ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                            text = stringResource(id = R.string.caption_text_connection_required),
                            style = MaterialTheme.typography.BodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                            painter = painterResource(R.drawable.ic_no_internet),
                            contentDescription = null
                    )
                }
            }
            SendButton(enabled = false)
        }
    }
}

@Composable
private fun SendButton(
    enabled: Boolean,
    onSend: () -> Unit = {}
) {
    val buttonBackground = remember(enabled) {
        if (enabled) ButtonPrimary else ButtonSecondary
    }
    Box(
            modifier = Modifier
                    .size(MaterialTheme.spacing.spaceDoubleDp * 22)
                    .clip(CircleShape)
                    .background(color = buttonBackground)
                    .clickable(enabled = enabled) { onSend() },
            contentAlignment = Alignment.Center
    ) {
        Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = null,

                tint = TextWhite
        )
    }
}




