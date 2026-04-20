package com.tonyxlab.smartstep.presentation.screens.chat.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.domain.ai.ChatMessage
import com.tonyxlab.smartstep.domain.ai.ChatRole
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.AssistantChatBubbleShape
import com.tonyxlab.smartstep.presentation.theme.BodyMediumMedium
import com.tonyxlab.smartstep.presentation.theme.BodyMediumRegular
import com.tonyxlab.smartstep.presentation.theme.ButtonPrimary
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.TextPrimary
import com.tonyxlab.smartstep.presentation.theme.TextSecondary
import com.tonyxlab.smartstep.presentation.theme.TextWhite
import com.tonyxlab.smartstep.presentation.theme.UserChatBubbleShape

@Composable
fun ChatSection(
    modifier: Modifier = Modifier
) {
    val messages = listOf(
            ChatMessage(
                    text = stringResource(R.string.chat_message_1),
                    role = ChatRole.ASSISTANT
            ),
            ChatMessage(
                    text = stringResource(R.string.chat_message_2),
                    role = ChatRole.USER
            ),
            ChatMessage(
                    text = stringResource(R.string.chat_message_1),
                    role = ChatRole.ASSISTANT
            ),
            ChatMessage(
                    text = stringResource(R.string.chat_message_2),
                    role = ChatRole.USER
            )
    )

    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
                modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(chatMessage = message)
            }
        }

        ChatInputSection()
    }
}

@Composable
private fun ChatBubble(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isAssistant = chatMessage.role == ChatRole.ASSISTANT
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
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
                shape = if (isAssistant)
                    MaterialTheme.shapes.AssistantChatBubbleShape
                else
                    MaterialTheme.shapes.UserChatBubbleShape,
                color = if (isAssistant) Color.Transparent else ButtonPrimary,
                border = if (isAssistant) borderStroke() else null,
                modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                    text = chatMessage.text,
                    modifier = Modifier.padding(12.dp),
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
private fun borderStroke() = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline
)

@Composable
private fun ChatInputSection(
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
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        Column(
                modifier = Modifier
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {
            Row(
                    // TODO: Add Click Listener Event 
                    modifier = Modifier.clickable { },
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
                        painter = painterResource(R.drawable.ic_chevron_up),
                        contentDescription = null,
                        tint = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

            Column(
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceMedium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
            ) {
                suggestions.fastForEach {
                    SuggestionItem(text = stringResource(it))
                }
            }



            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text(
                                    text = stringResource(R.string.label_step_ask_anything),
                                    style = MaterialTheme.typography.BodyMediumRegular,
                                    color = TextSecondary
                            )
                        },
                        modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(12.dp)
                                ),
                        colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                        modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ButtonPrimary)
                                .clickable { },
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = TextWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxWidth()
                    .border(
                            width = MaterialTheme.spacing.spaceSingleDp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                    )
                    // TODO: Add OnEvent Listner
                    .clickable { }
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

@Preview(showBackground = true)
@Composable
private fun ChatSectionPreview() {
    SmartStepTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            ChatSection()
        }
    }

}
