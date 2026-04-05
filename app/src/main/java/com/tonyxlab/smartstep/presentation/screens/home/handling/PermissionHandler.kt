@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.handling

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType

class PermissionHandler {

    fun showPermissionSheet(
        state: HomeUiState,
        type: PermissionSheetType
    ): HomeUiState {
        return state.copy(
                permissionUiState = state.permissionUiState.copy(
                        permissionSheetVisible = true,
                        permissionSheetType = type
                )
        )
    }

    fun dismissPermissionDialog(state: HomeUiState): HomeUiState {
        return state.copy(
                permissionUiState = state.permissionUiState.copy(
                        permissionSheetVisible = false,
                        permissionSheetType = null
                )
        )
    }

    fun closePermissionSheet(state: HomeUiState): HomeUiState {
        return state.copy(
                permissionUiState = state.permissionUiState.copy(
                        permissionSheetVisible = false
                )
        )
    }

    fun updateBackgroundAccessState(
        state: HomeUiState,
        granted: Boolean
    ): HomeUiState {
        return state.copy(
                permissionUiState = state.permissionUiState.copy(
                        isBackgroundAccessGranted = granted
                )
        )
    }

    fun updatePhysicalActivityPermissionRequested(
        state: HomeUiState,
        requested: Boolean
    ): HomeUiState {
        return state.copy(
                permissionUiState = state.permissionUiState.copy(
                        physicalActivityPermissionRequested = requested
                )
        )
    }
}