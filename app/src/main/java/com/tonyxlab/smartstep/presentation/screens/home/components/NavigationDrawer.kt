@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.EndVerticalRoundedCornerShape16
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import timber.log.Timber

@Composable
fun NavigationDrawer(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isBackgroundAccessGranted = uiState.permissionUiState.isBackgroundAccessGranted

    ModalDrawerSheet(
            modifier = modifier
                    .width(352.dp)
                    .fillMaxHeight(),
            drawerContainerColor = MaterialTheme.colorScheme.surface,
            drawerShape = MaterialTheme.shapes.EndVerticalRoundedCornerShape16
    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.spaceSmall)
        ) {
            if (isBackgroundAccessGranted == false) {
                DrawerItem(
                        text = stringResource(id = R.string.nav_drawer_fix_stop_counting),
                        onClick = { onEvent(HomeUiEvent.FixCountIssue) }
                )
                HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            }

            DrawerItem(
                    text = stringResource(id = R.string.nav_drawer_step_goal),
                    onClick = { onEvent(HomeUiEvent.ShowStepGoalSheet) }
            )

            HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            DrawerItem(
                    text = stringResource(id = R.string.nav_drawer_personal_settings),
                    onClick = { onEvent(HomeUiEvent.OpenPersonalSettings) }
            )

            HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            DrawerItem(
                    text = stringResource(id = R.string.nav_drawer_edit_steps),
                    onClick = {

                        Timber.tag("HomeScreen").i("Nav event sent")
                        onEvent(HomeUiEvent.EditSteps) }
            )

            HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            DrawerItem(
                    text = stringResource(id = R.string.nav_drawer_reset),
                    onClick = { onEvent(HomeUiEvent.ResetSteps) }
            )

            HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            DrawerItem(
                    text = stringResource(id = R.string.nav_drawer_exit),
                    textColor = MaterialTheme.colorScheme.primary,
                    onClick = { onEvent(HomeUiEvent.ShowExitDialog) }
            )
        }
    }
}

@Composable
private fun DrawerItem(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            modifier = modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
                    .padding(vertical = MaterialTheme.spacing.spaceMedium)
    )
}


@Preview(showBackground = true)
@Composable
private fun NavigationDrawerPreview() {
    SmartStepTheme {
        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            NavigationDrawer(
                    uiState = HomeUiState(),
                    onEvent = {}
            )
        }
    }
}
