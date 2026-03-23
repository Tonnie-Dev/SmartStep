@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.tonyxlab.smartstep.presentation.core.components.PermissionBottomSheet
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import timber.log.Timber

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
    val permissionState = rememberPermissionState(
            permission = Manifest.permission.ACTIVITY_RECOGNITION
    )

    val permissionStatus = permissionState.status

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var denialCount by rememberSaveable { mutableIntStateOf(0) }
    var hasShownBackgroundSheet by rememberSaveable { mutableStateOf(false) }

    // Launch the permission request only once when entering the screen
    LaunchedEffect(Unit) {
        if (!permissionStatus.isGranted && !hasRequestedPermission) {
            hasRequestedPermission = true
            permissionState.launchPermissionRequest()
        }
    }

    // React to permission result changes
    LaunchedEffect(permissionStatus) {
        when {
            permissionStatus.isGranted -> {
                if (!hasShownBackgroundSheet) {
                    hasShownBackgroundSheet = true



                    onEvent(
                            HomeUiEvent.ShowPermissionSheet(
                                    PermissionSheetType.BACKGROUND_ACCESS
                            )
                    )
                }
            }

            permissionStatus.shouldShowRationale -> {
                denialCount = 1



                onEvent(
                        HomeUiEvent.ShowPermissionSheet(
                                PermissionSheetType.INITIAL_DENIAL
                        )
                )

            }

            hasRequestedPermission || denialCount >= 1 -> {
                denialCount = 2


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

                    else -> {
                        onEvent(event)
                    }
                }
            }
    )
}


