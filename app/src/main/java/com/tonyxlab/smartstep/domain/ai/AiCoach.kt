package com.tonyxlab.smartstep.domain.ai

import kotlinx.coroutines.flow.StateFlow

interface AiCoach {

    val insightState: StateFlow<InsightState>
    val chatMessages: StateFlow<List<ChatMessage>>
    val chatState: StateFlow<ChatState>

    suspend fun refreshInsight(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    )

    suspend fun startChatSession(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    )

    suspend fun sendMessage(
        userMessage: String,
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        isOnline: Boolean
    )

    fun clearChatSession()
}
