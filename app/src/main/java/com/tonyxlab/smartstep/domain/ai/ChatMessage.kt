package com.tonyxlab.smartstep.domain.ai

enum class ChatRole {
    ASSISTANT,
    USER
}

data class ChatMessage(
    val role: ChatRole,
    val text: String
)