package com.tonyxlab.smartstep.presentation.screens.chat.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent
import com.tonyxlab.smartstep.presentation.core.base.handling.UiEvent

sealed interface ChatUiEvent : UiEvent{

    data object ExitChat: ChatUiEvent
}
