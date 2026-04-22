package com.tonyxlab.smartstep.domain.ai

sealed interface InsightState {
    data object Idle : InsightState
    data object Loading : InsightState
    data object Offline : InsightState
    data class Error(val message: String? = null) : InsightState
    data class Success(val insight: String) : InsightState
}

sealed interface ChatState {
    data object Idle : ChatState
    data object Loading : ChatState
    data object Offline : ChatState
    data class Error(val message: String? = null) : ChatState
}