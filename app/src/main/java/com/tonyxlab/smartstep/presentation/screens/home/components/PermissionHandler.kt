@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tonyxlab.smartstep.presentation.core.components.PermissionPrompt
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.utils.OnResumeEffect
import com.tonyxlab.smartstep.utils.isIgnoringBatteryOptimizations

enum class PermissionSheetType {
    INITIAL_DENIAL,
    PERMANENT_DENIAL,
    BACKGROUND_ACCESS
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionHandler(
    isDeviceWide: Boolean,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {

    val permissionState = rememberPermissionState(
            permission = Manifest.permission.ACTIVITY_RECOGNITION
    )
    val activity = LocalActivity.current ?: return
    val isBackgroundAccessGranted = activity.isIgnoringBatteryOptimizations()

    // Disposable Effect to Observe
    OnResumeEffect {
        if (!permissionState.status.isGranted) {
            onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.PERMANENT_DENIAL))
        }
    }

    val permissionStatus = permissionState.status

    // Launch the permission request only once when entering the screen for the first time
    LaunchedEffect(Unit) {
        if (!permissionStatus.isGranted && !uiState.physicalActivityPermissionRequested) {

            onEvent(HomeUiEvent.PhysicalActivityPermissionRequested)
            permissionState.launchPermissionRequest()
        }
    }

    // React to permission result changes or app launch
    LaunchedEffect(permissionStatus.isGranted, permissionStatus.shouldShowRationale) {
        when {
            permissionStatus.isGranted -> {
                if (
                    !isBackgroundAccessGranted &&
                    !uiState.backgroundPermissionSheetShown
                ) {
                    onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS))
                }
            }

            permissionStatus.shouldShowRationale -> {

                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.INITIAL_DENIAL))
            }

            uiState.physicalActivityPermissionRequested -> {

                // Not granted, no rationale, and we've requested before -> Permanent denial
                onEvent(
                        HomeUiEvent.ShowPermissionSheet(
                                PermissionSheetType.PERMANENT_DENIAL
                        )
                )
            }

        }
    }

    PermissionPrompt(
            isDeviceWide = isDeviceWide,
            isSheetVisible = uiState.isSheetVisible,
            permissionSheetType = uiState.permissionSheetType,
            hasHandle = uiState.permissionSheetType == PermissionSheetType.BACKGROUND_ACCESS,
            onEvent = { event ->
                when (event) {
                    HomeUiEvent.AllowAccess -> {
                        onEvent(HomeUiEvent.DismissPermissionDialog)
                        permissionState.launchPermissionRequest()
                    }

                    HomeUiEvent.Continue -> {
                        onEvent(HomeUiEvent.ShowBackgroundPermissionSheet)
                        onEvent(event)
                    }

                    else -> {
                        onEvent(event)
                    }
                }
            }
    )
}
