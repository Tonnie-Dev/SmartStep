package com.tonyxlab.smartstep.presentation.screens.chat.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ChatUiEvent : UiEvent{

    data object SendChatMessage: ChatUiEvent
    data object ToggleSuggestionsDrawer: ChatUiEvent
    data class SelectPrompt(val prompt: String): ChatUiEvent
    data object ExitChat: ChatUiEvent
}
