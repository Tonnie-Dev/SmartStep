@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tonyxlab.smartstep.presentation.core.components.PermissionBottomSheet
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState

enum class PermissionSheetType {
    INITIAL_DENIAL,
    PERMANENT_DENIAL,
    BACKGROUND_ACCESS
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionHandler(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(
            permission = Manifest.permission.ACTIVITY_RECOGNITION
    )

    DisposableEffect(lifecycleOwner, permissionState.status) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = permissionState.status.isGranted

                if (!isGranted) {
                    onEvent(
                            HomeUiEvent.ShowPermissionSheet(
                                    PermissionSheetType.PERMANENT_DENIAL
                            )
                    )
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                if (!uiState.backgroundPermissionSheetShown) {
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

    PermissionBottomSheet(
            isSheetVisible = uiState.isSheetVisible,
            permissionSheetType = uiState.permissionSheetType,
            onEvent = { event ->
                when (event) {
                    HomeUiEvent.AllowAccess -> {
                        onEvent(HomeUiEvent.DismissPermissionDialog)
                        permissionState.launchPermissionRequest()
                    }

                    HomeUiEvent.Continue -> {
                        onEvent(HomeUiEvent.BackgroundPermissionSheetShown)
                        onEvent(event)
                    }

                    else -> {
                        onEvent(event)
                    }
                }
            }
    )
}
