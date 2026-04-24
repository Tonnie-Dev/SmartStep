package com.tonyxlab.smartstep.presentation.screens.chat.handling

import androidx.compose.foundation.text.input.TextFieldState
import com.tonyxlab.smartstep.domain.ai.ChatMessage
import com.tonyxlab.smartstep.domain.ai.ChatState
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

data class ChatUiState(
    val isOnline: Boolean = true,
    val textFieldState: TextFieldState = TextFieldState(),
    val suggestionsExpanded: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),
    val chatState: ChatState = ChatState.Idle,
    val stepCount: Int = 0,
    val dailyGoal: Int = 0
) : UiState
