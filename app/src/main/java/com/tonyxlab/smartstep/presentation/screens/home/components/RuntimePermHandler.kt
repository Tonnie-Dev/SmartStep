@file:RequiresApi(Build.VERSION_CODES.Q)

/*
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionUiHandler(
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
    val permissionUiState = uiState.permissionUiState
    val physicalActivityPermissionRequested = permissionUiState
            .physicalActivityPermissionRequested

    // Launch the permission request only once when entering the screen for the first time
    LaunchedEffect(Unit) {
        if (!permissionStatus.isGranted && !physicalActivityPermissionRequested) {

            onEvent(HomeUiEvent.PhysicalActivityPermissionRequested)
            permissionState.launchPermissionRequest()
        }
    }

    // React to permission result changes or app launch
    LaunchedEffect(permissionStatus.isGranted, permissionStatus.shouldShowRationale) {
        when {
            permissionStatus.isGranted -> {
                if (!isBackgroundAccessGranted

                ) {
                    onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS))
                }
            }

            permissionStatus.shouldShowRationale -> {
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.INITIAL_DENIAL))
            }

            physicalActivityPermissionRequested -> {

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
            isSheetVisible = permissionUiState.permissionSheetVisible,
            permissionSheetType = permissionUiState.permissionSheetType,
            hasHandle = permissionUiState.permissionSheetType == PermissionSheetType.BACKGROUND_ACCESS,
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
*/


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
    Timber.tag("PermHandler")
            .i("Top Level - is activity perm Granted: ${activityPermissionState.status.isGranted}")
    val isBackgroundAccessGranted = activity.isIgnoringBatteryOptimizations()

    val notificationPermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    val permissionUiState = uiState.permissionUiState
    val physicalActivityPermissionRequested =
        permissionUiState.physicalActivityPermissionRequested

    val activityGranted = activityPermissionState.status.isGranted

    val notificationGranted =
        notificationPermissionState?.status?.isGranted ?: true

    OnResumeEffect {
        when {
            !activityGranted -> {

                Timber.tag("PermHandler")
                        .i("Act. Perm Status on Resume: ${activityPermissionState.status.isGranted}")
                Timber.tag("PermHandler")
                        .i("Checking Activity on Resume Effect")
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.PERMANENT_DENIAL))
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    notificationPermissionState?.status?.isGranted == false &&
                    notificationPermissionState.status.shouldShowRationale -> {

                Timber.tag("PermHandler")
                        .i("Checking Notif on Resume Effect")
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.INITIAL_DENIAL))
            }
        }
    }

    // First request activity recognition once
    LaunchedEffect(Unit) {
        if (!activityGranted && !physicalActivityPermissionRequested) {
            onEvent(HomeUiEvent.PhysicalActivityPermissionRequested)
            activityPermissionState.launchPermissionRequest()
        }
    }

    // Once activity recognition is granted, request notifications on Android 13+
    LaunchedEffect(activityGranted) {
        if (
            activityGranted &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            notificationPermissionState?.status?.isGranted == false
        ) {
            notificationPermissionState.launchPermissionRequest()
        }
    }

    // Decide what UI sheet to show next
    LaunchedEffect(
            activityGranted,
            activityPermissionState.status.shouldShowRationale,
            notificationGranted,
            notificationPermissionState?.status?.shouldShowRationale,
            isBackgroundAccessGranted
    ) {
        when {
            !activityGranted && activityPermissionState.status.shouldShowRationale -> {
                Timber.tag("PermHandler")
                        .i("When Stub 1")
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.INITIAL_DENIAL))
            }

            !activityGranted && physicalActivityPermissionRequested -> {

                Timber.tag("PermHandler")
                        .i("When Stub 2")
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.PERMANENT_DENIAL))
            }

            activityGranted && notificationGranted && !isBackgroundAccessGranted -> {

                Timber.tag("PermHandler")
                        .i("When Stub 3")
                onEvent(HomeUiEvent.ShowPermissionSheet(PermissionSheetType.BACKGROUND_ACCESS))
            }
        }
    }

    PermissionPrompt(
            isDeviceWide = isDeviceWide,
            isSheetVisible = permissionUiState.permissionSheetVisible,
            permissionSheetType = permissionUiState.permissionSheetType,
            hasHandle = permissionUiState.permissionSheetType == PermissionSheetType.BACKGROUND_ACCESS,
            onEvent = { event ->
                when (event) {
                    HomeUiEvent.AllowAccess -> {
                        onEvent(HomeUiEvent.DismissPermissionDialog)

                        when {
                            !activityPermissionState.status.isGranted -> {
                                activityPermissionState.launchPermissionRequest()
                            }

                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    notificationPermissionState?.status?.isGranted == false -> {
                                notificationPermissionState.launchPermissionRequest()
                            }
                        }
                    }

                    HomeUiEvent.Continue -> {
                        onEvent(HomeUiEvent.ShowBackgroundPermissionSheet)
                        onEvent(event)
                    }

                    else -> onEvent(event)
                }
            }
    )
}