package com.tonyxlab.smartstep.presentation.screens.chat.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.ActionEvent

sealed interface ChatActionEvent : ActionEvent{

    data object NavigateToHome: ChatActionEvent
}