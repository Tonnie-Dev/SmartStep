@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.ai

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.domain.ai.AiCoach
import com.tonyxlab.smartstep.domain.ai.ChatMessage
import com.tonyxlab.smartstep.domain.ai.ChatRole
import com.tonyxlab.smartstep.domain.ai.ChatState
import com.tonyxlab.smartstep.domain.ai.InsightState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AiCoachImpl(private val aiClient: AiClient) : AiCoach {

    private val _insightState = MutableStateFlow<InsightState>(InsightState.Idle)
    override val insightState: StateFlow<InsightState>
        get() = _insightState.asStateFlow()

    // Cache so we don't regenerate on minor step changes
    private var lastInsight: String? = null

    // --- Chat state ---

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val chatMessages: StateFlow<List<ChatMessage>>
        get() = _chatMessages.asStateFlow()

    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    override val chatState: StateFlow<ChatState>
        get() = _chatState.asStateFlow()

    /*   private val _chatLoading = MutableStateFlow(false)
       override val chatLoading: StateFlow<Boolean>
           get() = _chatLoading.asStateFlow()*/

    override suspend fun refreshInsight(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    ) {
        if (!isOnline) {
            _insightState.value = InsightState.Offline
            return
        }

        _insightState.value = InsightState.Loading
        aiClient.generateInsight(currentSteps, dailyGoal, progress)
                .onSuccess { insight ->
                    lastInsight = insight
                    _insightState.value = InsightState.Success(insight)
                }
                .onFailure {
                    // Fall back to cached insight if available
                    val cached = lastInsight
                    _insightState.value = if (cached != null) {
                        InsightState.Success(cached)
                    } else {
                        InsightState.Error(it.message)
                    }
                }
    }

    override suspend fun startChatSession(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    ) {
        if (_chatMessages.value.isNotEmpty()) return

        if (!isOnline) {
            _chatState.value = ChatState.Offline
            return
        }

        _chatState.value = ChatState.Loading

        aiClient.generateInitialChatMessage(
                currentSteps = currentSteps,
                dailyGoal = dailyGoal,
                progress = progress
        )
                .onSuccess { reply ->
                    appendMessage(
                            ChatMessage(
                                    role = ChatRole.ASSISTANT,
                                    text = reply
                            )
                    )
                }
                .onFailure {
                    appendMessage(
                            ChatMessage(
                                    role = ChatRole.ASSISTANT,
                                    text = "Hello! I'm your fitness coach. You're getting started today. How can I help?"
                            )
                    )
                    _chatState.value = ChatState.Error(it.message)
                }

    }

    override suspend fun sendMessage(
        userMessage: String,
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    ) {

        if (!isOnline) {
            _chatState.value = ChatState.Offline
            return
        }

        val historyBeforeNewMessage = _chatMessages.value.map { it.role to it.text }

        // Add user message immediately so the UI feels responsive
        appendMessage(ChatMessage(role = ChatRole.USER, text = userMessage))
        _chatState.value = ChatState.Loading

        aiClient.generateChatReply(
                currentSteps = currentSteps,
                dailyGoal = dailyGoal,
                progress = progress,
                conversationHistory = historyBeforeNewMessage,
                userMessage = userMessage
        )
                .onSuccess { reply ->
                    appendMessage(ChatMessage(role = ChatRole.ASSISTANT, text = reply))
                }
                .onFailure {
                    appendMessage(
                            ChatMessage(
                                    role = ChatRole.ASSISTANT,
                                    text = "Sorry, I couldn't respond. Please try again."
                            )
                    )
                    _chatState.value = ChatState.Error(it.message)
                }
    }

    override fun clearChatSession() {
        _chatMessages.value = emptyList()
    }

    private fun appendMessage(message: ChatMessage) {
        _chatMessages.value += message
    }
}