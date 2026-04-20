
@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.smartstep.data.ai

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.domain.ai.AiCoach
import com.tonyxlab.smartstep.domain.ai.InsightState
import com.tonyxlab.smartstep.domain.ai.ChatMessage
import com.tonyxlab.smartstep.domain.ai.ChatRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.map

class AiCoachImpl(private val aiClient: AiClient): AiCoach {

    private val _insightState = MutableStateFlow<InsightState>(InsightState.Idle)
    override val insightState: StateFlow<InsightState>
        get() = _insightState

    // --- Insight block state ---




    // Cache so we don't regenerate on minor step changes
    private var lastInsight: String? = null

    // --- Chat state ---

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _chatLoading = MutableStateFlow(false)
    val chatLoading: StateFlow<Boolean> = _chatLoading.asStateFlow()

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
        progress: Float
    ) {
        _chatMessages.value = emptyList()
        _chatLoading.value = true

        aiClient.generateChatReply(
                currentSteps = currentSteps,
                dailyGoal = dailyGoal,
                progress = progress,
                conversationHistory = emptyList(),
                userMessage = "Start the session with a greeting and ask how you can help."
        ).onSuccess { reply ->
            appendMessage(ChatMessage(role = ChatRole.ASSISTANT, text = reply))
        }.onFailure {
            appendMessage(
                    ChatMessage(
                            role = ChatRole.ASSISTANT,
                            text = "Hi! I'm your AI coach. How can I help you today?"
                    )
            )
        }

        _chatLoading.value = false
    }

    override suspend fun sendMessage(
        userMessage: String,
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float
    ) {
        // Add user message immediately so the UI feels responsive
        appendMessage(ChatMessage(role = ChatRole.USER, text = userMessage))
        _chatLoading.value = true

        val history = _chatMessages.value
                .map { it.role to it.text }

        aiClient.generateChatReply(
                currentSteps = currentSteps,
                dailyGoal = dailyGoal,
                progress = progress,
                conversationHistory = history,
                userMessage = userMessage
        ).onSuccess { reply ->
            appendMessage(ChatMessage(role = ChatRole.ASSISTANT, text = reply))
        }.onFailure {
            appendMessage(
                    ChatMessage(
                            role = ChatRole.ASSISTANT,
                            text = "Sorry, I couldn't respond. Please try again."
                    )
            )
        }

        _chatLoading.value = false
    }

    override fun clearChatSession() {
        _chatMessages.value = emptyList()
    }

    private fun appendMessage(message: ChatMessage) {
        _chatMessages.value += message
    }
}