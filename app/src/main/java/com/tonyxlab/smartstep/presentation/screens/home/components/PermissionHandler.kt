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

enum class PermissionSheetType {
    INITIAL_DENIAL,
    PERMANENT_DENIAL,
    BACKGROUND_ACCESS
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)

@Composable
fun PermissionHandler(

    onEvent: (HomeUiEvent) -> Unit,
) {

    val activityRecognitionPermissionState = rememberPermissionState(
            permission = Manifest.permission.ACTIVITY_RECOGNITION
    )

    val permissionStatus = activityRecognitionPermissionState.status

    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var hasShownBackgroundSheet by rememberSaveable { mutableStateOf(false) }

    var denialCount by rememberSaveable { mutableIntStateOf(0) }

    var permissionSheetType by rememberSaveable {
        mutableStateOf<PermissionSheetType?>(null)
    }



    // Launch the permission request only once when entering the screen
    LaunchedEffect(Unit) {
        if (!permissionStatus.isGranted && !hasRequestedPermission) {
            hasRequestedPermission = true
            activityRecognitionPermissionState.launchPermissionRequest()
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
                onEvent(
                        HomeUiEvent.ShowPermissionSheet(
                                PermissionSheetType.INITIAL_DENIAL
                        )
                )
            }

            hasRequestedPermission -> {
                onEvent(
                        HomeUiEvent.ShowPermissionSheet(
                                PermissionSheetType.PERMANENT_DENIAL
                        )
                )
            }
        }
    }

  /*  permissionSheetType?.let { sheetType ->

        PermissionBottomSheet(
                isSheetVisible = isSheetVisible,
                permissionSheetType = sheetType,
                hasHandle = sheetType == PermissionSheetType.BACKGROUND_ACCESS,
                onEvent = { event ->

                    when (event) {
                        HomeUiEvent.AllowAccess -> {
                            activityRecognitionPermissionState.launchPermissionRequest()
                            hasRequestedPermission = true
                        }

                        else -> onEvent(event)
                    }
                })
    }*/

}