package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape28

@Composable
fun LargeScreenDialog(
    onEvent: (HomeUiEvent) -> Unit,
    isLocked: Boolean,
    dialogContent: @Composable () -> Unit
) {

    Dialog(
            onDismissRequest = {
                if (!isLocked) {
                    onEvent(HomeUiEvent.DismissPermissionDialog)
                }
            },
            properties = DialogProperties(
                    dismissOnClickOutside = !isLocked,
                    dismissOnBackPress = !isLocked
            )
    ) {
        Surface(
                modifier = Modifier.requiredWidth(412.dp),
                shape = MaterialTheme.shapes.RoundedCornerShape28) {

            Box(
                    modifier = Modifier.padding(
                            horizontal = MaterialTheme.spacing.spaceMedium,
                            vertical = MaterialTheme.spacing.spaceMedium
                    )
            ) {
                dialogContent()
            }
        }
    }
}