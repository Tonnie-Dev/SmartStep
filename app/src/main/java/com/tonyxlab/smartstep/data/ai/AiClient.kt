@file:RequiresApi(Build.VERSION_CODES.O)
package com.tonyxlab.smartstep.data.ai

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.ai.client.generativeai.GenerativeModel
import com.tonyxlab.smartstep.BuildConfig
import com.tonyxlab.smartstep.domain.ai.ChatRole
import com.tonyxlab.smartstep.utils.getTimeOfTheDay

class AiClient {

    private val model = GenerativeModel(
            modelName = "gemini-3-flash-preview",
            apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateInsight(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float
    ): Result<String> = runCatching {

        val prompt = buildInsightPrompt(currentSteps, dailyGoal, progress)
        val response = model.generateContent(prompt)
        response.text?.trim()
            ?: error("Empty response from AI")
    }

    suspend fun generateChatReply(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        conversationHistory: List<Pair<ChatRole, String>>, // role to text
        userMessage: String
    ): Result<String> = runCatching {

        val history = conversationHistory.joinToString("\n") { (role, text) ->
            "${role.name.replaceFirstChar { it.uppercase() }}: $text"
        }

        val prompt = buildChatPrompt(currentSteps, dailyGoal, progress, history, userMessage)
        val response = model.generateContent(prompt)
        response.text?.trim()
            ?: error("Empty response from AI")
    }

    private fun buildInsightPrompt(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float
    ): String = """
        You are an AI fitness insight assistant inside a step tracking app.

        Generate exactly one short insight message for the user based only on the structured input below.

        Structured input:
        - current_step_count: $currentSteps
        - daily_step_goal: $dailyGoal
        - goal_completion_percent: $progress
        - time_context: ${getTimeOfTheDay()}

        Rules:
        - Return only one short message.
        - Keep it under 20 words.
        - Use a motivational or analytical tone.
        - Do not ask questions.
        - Do not continue a conversation.
        - Do not mention medical advice.
        - Do not mention that you are an AI.
        - Do not repeat all raw numeric values unnecessarily.
        - Do not use emojis.
        - Output plain text only.

        Good examples:
        - You're on track today. Keep the pace steady.
        - You're a bit behind your goal — a short walk could help.
        - You are slight ly behind today ’s pace — 1.2k steps needed.
        - Great job! You've already reached today's goal.
    """.trimIndent()

    private fun buildChatPrompt(
        currentSteps: Int,
        dailyGoal: Int,
        progress: Float,
        history: String,
        userMessage: String
    ): String = """
        You are a personal AI fitness coach inside a step tracking app.
        You give short, helpful, motivational fitness advice based on the user's activity.
        You do not give medical advice. You do not mention that you are an AI.

        User's current context:
        - current_step_count: $currentSteps
        - daily_step_goal: $dailyGoal
        - goal_completion_percent: $progress
        - time_context: ${getTimeOfTheDay()}

        Conversation so far:
        $history

        User: $userMessage
        Assistant:
    """.trimIndent()
}