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
import timber.log.Timber

enum class PermissionSheetType {
    INITIAL_DENIAL,
    PERMANENT_DENIAL,
    NOTIFICATION_ACCESS,
    BACKGROUND_ACCESS
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionUiHandler(
    isDeviceWide: Boolean,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {
    val activity = LocalActivity.current ?: return

    val activityPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACTIVITY_RECOGNITION
    )

    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    val isBackgroundAccessGranted = activity.isIgnoringBatteryOptimizations()

    val permissionUiState = uiState.permissionUiState
    val physicalActivityPermissionRequested = permissionUiState.physicalActivityPermissionRequested

    val activityGranted = activityPermissionState.status.isGranted
    val notificationGranted = notificationPermissionState?.status?.isGranted ?: true

    // Report background access state to ViewModel for service management
    LaunchedEffect(isBackgroundAccessGranted) {
        onEvent(HomeUiEvent.BackgroundAccessChanged(isBackgroundAccessGranted))
    }

    // OnResume check for mandatory Activity permission
    OnResumeEffect {
        if (!activityGranted && physicalActivityPermissionRequested) {
            onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.PERMANENT_DENIAL))
        }
    }

    // 1. Initial request for Activity Recognition (Automatic on first launch)
    LaunchedEffect(Unit) {
        if (!activityGranted && !physicalActivityPermissionRequested) {
            onEvent(HomeUiEvent.PhysicalActivityPermissionRequested)
            activityPermissionState.launchPermissionRequest()
        }
    }

    // 2. Initial request for Notifications (Automatic after Activity is granted on Android 13+)
    LaunchedEffect(activityGranted) {
        if (activityGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionState?.let {
                if (!it.status.isGranted && !it.status.shouldShowRationale) {
                    it.launchPermissionRequest()
                }
            }
        }
    }

    // 3. Logic to show rationale or instruction sheets automatically
    LaunchedEffect(
        activityGranted,
        activityPermissionState.status.shouldShowRationale,
        notificationGranted,
        notificationPermissionState?.status?.shouldShowRationale,
        isBackgroundAccessGranted,
        //permissionUiState.permissionSheetVisible
    ) {
        // Skip if a sheet is already showing to avoid overlapping
        if (permissionUiState.permissionSheetVisible) return@LaunchedEffect

        when {
            // Activity Recognition: Mandatory
            !activityGranted -> {
                if (activityPermissionState.status.shouldShowRationale) {
                    onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.INITIAL_DENIAL))
                } else if (physicalActivityPermissionRequested) {
                    onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.PERMANENT_DENIAL))
                }
            }

            // Notification: Recommended
            !notificationGranted -> {
                if (notificationPermissionState.status.shouldShowRationale) {
                    onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.NOTIFICATION_ACCESS))
                }
            }

            // Background Access: Recommended for tracking reliability
            !isBackgroundAccessGranted -> {
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS))
            }
        }
    }
Timber.tag("Perm Handler").i("About to Call PermissionPrompt, visibility is: ${permissionUiState.permissionSheetVisible}, Nullability is: ${permissionUiState.permissionSheetType}")
    PermissionPrompt(
        isDeviceWide = isDeviceWide,
        isSheetVisible = permissionUiState.permissionSheetVisible,
        permissionSheetType = permissionUiState.permissionSheetType,
        hasHandle = permissionUiState.permissionSheetType == PermissionSheetType.BACKGROUND_ACCESS ||
                   permissionUiState.permissionSheetType == PermissionSheetType.NOTIFICATION_ACCESS,
        onEvent = { event ->
            when (event) {
                HomeUiEvent.AllowAccess -> {
                    onEvent(HomeUiEvent.DismissPermissionDialog)
                    when {
                        !activityGranted -> {
                            activityPermissionState.launchPermissionRequest()
                        }
                        !notificationGranted -> {
                            notificationPermissionState.launchPermissionRequest()
                        }
                    }
                }

                HomeUiEvent.Continue -> {

                  onEvent(HomeUiEvent.DismissPermissionDialog)
                    onEvent(event) // ViewModel handles RequestBatteryOptimization
                }

                else -> onEvent(event)
            }
        }
    )
}
