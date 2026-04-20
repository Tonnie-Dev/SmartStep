@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.domain.ai.InsightState

class InsightHandler {

    fun handleInsight(
        state: HomeUiState,
        insightState: InsightState
    ): HomeUiState {

        return when (insightState) {

            InsightState.Idle -> state.copy(
                    insightMessageState = state.insightMessageState.copy(
                            isInsightLoading = false
                    )
            )

            InsightState.Loading -> state.copy(
                    insightMessageState = state.insightMessageState.copy(
                            isInsightLoading = true
                    )
            )

            is InsightState.Success -> state.copy(
                    insightMessageState = state.insightMessageState.copy(
                            isInsightLoading = false,
                            insightMessage = insightState.insight
                    )
            )

            InsightState.Offline -> state.copy(
                    insightMessageState = state.insightMessageState.copy(
                            isInsightLoading = false
                    )
            )

            is InsightState.Error -> state.copy(
                    insightMessageState = state.insightMessageState.copy(
                            isInsightLoading = false,
                            insightMessage = "AI insight is temporarily unavailable. \nPlease try again later"
                    )
            )
        }
    }
}