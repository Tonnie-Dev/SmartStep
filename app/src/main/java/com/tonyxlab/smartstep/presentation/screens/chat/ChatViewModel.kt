package com.tonyxlab.smartstep.presentation.screens.chat

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatActionEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiState

typealias ChatBaseViewModel = BaseViewModel<ChatUiState, ChatUiEvent, ChatActionEvent>

class ChatViewModel : ChatBaseViewModel() {

    override val initialState: ChatUiState
        get() = ChatUiState()

    override fun onEvent(event: ChatUiEvent) {
        when (event) {
            ChatUiEvent.ExitChat -> {

                sendActionEvent(ChatActionEvent.NavigateToHome)
            }
        }
    }
}