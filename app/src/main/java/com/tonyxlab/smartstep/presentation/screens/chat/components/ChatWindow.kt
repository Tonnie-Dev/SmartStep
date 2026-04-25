package com.tonyxlab.smartstep.presentation.screens.chat.components

import android.R.attr.text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.domain.ai.ChatMessage
import com.tonyxlab.smartstep.domain.ai.ChatRole
import com.tonyxlab.smartstep.domain.ai.ChatState
import com.tonyxlab.smartstep.presentation.core.components.AppInputField
import com.tonyxlab.smartstep.presentation.core.components.ShimmerEffect
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiState
import com.tonyxlab.smartstep.presentation.theme.AssistantChatBubbleShape
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.BodyMediumRegular
import com.tonyxlab.smartstep.presentation.theme.ButtonPrimary
import com.tonyxlab.smartstep.presentation.theme.ButtonSecondary
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.TextPrimary
import com.tonyxlab.smartstep.presentation.theme.TextWhite
import com.tonyxlab.smartstep.presentation.theme.UserChatBubbleShape
import com.tonyxlab.smartstep.utils.borderStroke
import com.tonyxlab.smartstep.utils.ifThen

@Composable
fun ChatWindow(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit
) {
    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)

    ) {
        LazyColumn(
                modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                contentPadding = PaddingValues(all = MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            items(uiState.chatMessages) { message ->
                ChatBubble(chatMessage = message)
            }

            if (uiState.chatState == ChatState.Loading) {
                item {
                    AssistantTypingBubble()
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        ChatInputSection(
                modifier = Modifier
                        .imePadding()
                        .padding(bottom = MaterialTheme.spacing.spaceSmall),
                uiState = uiState,
                onEvent = onEvent
        )
    }
}

@Composable
private fun ChatBubble(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isAssistant = chatMessage.role == ChatRole.ASSISTANT
    val screenWidth = LocalWindowInfo.current.containerSize.width.dp

    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = if (isAssistant) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.Top
    ) {
        if (isAssistant) {
            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceLarge)
                            .clip(CircleShape)
                            .background(ButtonPrimary),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(R.drawable.ic_robot),
                        contentDescription = null,
                        tint = TextWhite
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))
        }

        Surface(
                modifier = Modifier
                        .ifThen(isAssistant) {
                            weight(1f)
                        }
                        .ifThen(isAssistant.not()) {
                            widthIn(max = screenWidth * .75f)
                        },
                shape = if (isAssistant)
                    MaterialTheme.shapes.AssistantChatBubbleShape
                else
                    MaterialTheme.shapes.UserChatBubbleShape,
                color = if (isAssistant) Color.Transparent else ButtonPrimary,
                border = if (isAssistant) borderStroke() else null
        ) {
            Text(
                      text = if (isAssistant) {
                        remember(chatMessage.text) {
                            markdownToAnnotatedString(chatMessage.text)
                        }
                    } else {
                        AnnotatedString(chatMessage.text)
                    },
                    modifier = Modifier.padding(MaterialTheme.spacing.spaceMedium),
                    style = if (isAssistant) {
                        MaterialTheme.typography.BodyMediumRegular.copy(color = TextPrimary)
                    } else {
                        MaterialTheme.typography.BodyMediumRegular.copy(color = TextWhite)
                    }
            )
        }
    }
}

@Composable
private fun AssistantTypingBubble(
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
    ) {
        Box(
                modifier = Modifier
                        .size(MaterialTheme.spacing.spaceLarge)
                        .clip(CircleShape)
                        .background(ButtonPrimary),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    painter = painterResource(R.drawable.ic_robot),
                    contentDescription = null,
                    tint = TextWhite
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))

        Surface(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.AssistantChatBubbleShape,
                color = Color.Transparent,
                border = borderStroke()
        ) {
            Box(
                    modifier = Modifier.padding(MaterialTheme.spacing.spaceMedium)
            ) {
                ShimmerEffect(
                        modifier = Modifier.fillMaxWidth()
                )
            }
        }
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

    Column(modifier = modifier) {

        QuickSuggestionsSection(
                modifier = Modifier,
                expanded = uiState.suggestionsExpanded,
                onToggleSuggestionsDrawer = { onEvent(ChatUiEvent.ToggleSuggestionsDrawer) },
                onSelectPrompt = { onEvent(ChatUiEvent.SelectPrompt((it))) }
        )

        Row(
                modifier = Modifier
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
}



private fun markdownToAnnotatedString(markdown: String): AnnotatedString {
    return buildAnnotatedString {
        val cleanedText = markdown
                .replace(Regex("^\\s*\\*\\s+", RegexOption.MULTILINE), "• ")

        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
        var currentIndex = 0

        boldRegex.findAll(cleanedText).forEach { match ->
            append(cleanedText.substring(currentIndex, match.range.first))

            val boldText = match.groupValues[1]

            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(boldText)
            pop()

            currentIndex = match.range.last + 1
        }

        append(cleanedText.substring(currentIndex))
    }
}
@Preview(showBackground = true)
@Composable
private fun ChatWindowPreview() {
    SmartStepTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            ChatWindow(
                    uiState = ChatUiState(),
                    onEvent = {})
        }
    }
}
