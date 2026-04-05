@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi

class ResetExitHandler {

    // Reset
    fun showResetDialog(state: HomeUiState): HomeUiState {
        return state.copy(showResetDialog = true)
    }

    fun confirmResetDialog(state: HomeUiState): HomeUiState {
        return state.copy(
                showResetDialog = false,
                currentSteps = 0
        )
    }

    fun dismissResetDialog(state: HomeUiState): HomeUiState {
        return state.copy(showResetDialog = false)
    }

    //Exit
    fun showExitDialog(state: HomeUiState): HomeUiState {
        return state.copy(showExitDialog = true)
    }

    fun closeExitDialog(state: HomeUiState): HomeUiState {
        return state.copy(showExitDialog = false)
    }
}