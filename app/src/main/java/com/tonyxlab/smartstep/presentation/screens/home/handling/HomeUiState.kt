package com.tonyxlab.smartstep.presentation.screens.home.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType

data class HomeUiState(
    val isSheetVisible: Boolean = false,
    val permissionSheetType: PermissionSheetType? = null,
    val physicalActivityPermissionRequested: Boolean = false,
    val isBackgroundAccessGranted: Boolean? = null
) : UiState
