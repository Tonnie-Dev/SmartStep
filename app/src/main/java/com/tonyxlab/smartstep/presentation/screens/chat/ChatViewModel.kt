@file:OptIn(FlowPreview::class)

package com.tonyxlab.smartstep.presentation.screens.chat

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.domain.ai.AiCoach
import com.tonyxlab.smartstep.domain.connectivity.ConnectivityObserver
import com.tonyxlab.smartstep.domain.repository.ActivityStats
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatActionEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias ChatBaseViewModel = BaseViewModel<ChatUiState, ChatUiEvent, ChatActionEvent>

class ChatViewModel(
    private val aiCoach: AiCoach,
    private val connectivityObserver: ConnectivityObserver,
    private val activityStats: ActivityStats
) : ChatBaseViewModel() {

    private var hasStartedChatSession = false

    override val initialState: ChatUiState
        get() = ChatUiState()

    init {
        observeNetwork()
        observeStats()
        observeChat()
    }

    override fun onEvent(event: ChatUiEvent) {
        when (event) {
            ChatUiEvent.SendChatMessage -> sendMessage()
            is ChatUiEvent.SelectPrompt -> selectPrompt(event.prompt)
            ChatUiEvent.ToggleSuggestionsDrawer -> toggleSuggestionsDrawer()
            ChatUiEvent.ExitChat -> exitChat()
        }
    }

    private fun observeNetwork() {

        connectivityObserver.isOnline()
                .onEach { isOnline ->
                    updateState { it.copy(isOnline = isOnline) }
                }
                .launchIn(viewModelScope)
    }

    private fun observeStats() {

        combine(
                activityStats.stepCount,
                activityStats.dailyGoal
        ) { steps, goal ->
            steps to goal
        }
                .onEach { (steps, goal) ->
                    updateState { it.copy(stepCount = steps, dailyGoal = goal) }
                    if (!hasStartedChatSession && goal > 0) {
                        hasStartedChatSession = true
                       startChatSession()
                    }
                }
                .launchIn(viewModelScope)

    }

    private fun observeChat() {
        launch {
            combine(aiCoach.chatMessages, aiCoach.chatState) { messages, chatState ->
                messages to chatState
            }.onEach { (messages, chatState) ->
                updateState {
                    it.copy(
                            chatMessages = messages,
                            chatState = chatState
                    )
                }
            }
                    .launchIn(viewModelScope)
        }
    }

    private fun startChatSession() {

        launch {
            with(currentState) {

                aiCoach.startChatSession(
                        currentSteps = stepCount,
                        dailyGoal = dailyGoal,
                        progress = calculateProgress(
                                stepCount = stepCount,
                                dailyGoal = dailyGoal
                        ),
                        isOnline = isOnline
                )
            }
        }
    }

    private fun sendMessage() {
        val userMessage = currentState.textFieldState.text
                .toString()
                .trim()


        if (userMessage.isBlank()) return
        launch {
            with(currentState) {
                aiCoach.sendMessage(
                        userMessage = userMessage,
                        currentSteps = stepCount,
                        dailyGoal = dailyGoal,
                        progress = calculateProgress(
                                stepCount = stepCount,
                                dailyGoal = dailyGoal
                        ),
                        isOnline = isOnline
                )
            }
        }

        currentState.textFieldState.edit {
            delete(0, length)
        }

        updateState { it.copy(suggestionsExpanded = false) }
    }

    private fun toggleSuggestionsDrawer() {
        updateState { it.copy(suggestionsExpanded = !currentState.suggestionsExpanded) }
    }

    private fun selectPrompt(prompt: String) {
        updateState {
            it.copy(
                    textFieldState = TextFieldState(initialText = prompt),
                    suggestionsExpanded = false
            )
        }
        sendMessage()
    }

    private fun calculateProgress(
        stepCount: Int,
        dailyGoal: Int
    ): Float {

        if (dailyGoal <= 0) return 0f
        return stepCount.toFloat() / dailyGoal
    }

    private fun exitChat() {
        aiCoach.clearChatSession()
        hasStartedChatSession = false
        updateState { ChatUiState() }
        sendActionEvent(ChatActionEvent.NavigateToHome)
    }
}