package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun AppNavigationDrawer(
    modifier: Modifier = Modifier,
    onFixIssueClick: () -> Unit = {},
    onStepGoalClick: () -> Unit = {},
    onPersonalSettingsClick: () -> Unit = {},
    onExitClick: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(300.dp)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            DrawerItem(
                text = stringResource(id = R.string.nav_drawer_fix_stop_counting),
                onClick = onFixIssueClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            DrawerItem(
                text = stringResource(id = R.string.nav_drawer_step_goal),
                onClick = onStepGoalClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            DrawerItem(
                text = stringResource(id = R.string.nav_drawer_personal_settings),
                onClick = onPersonalSettingsClick
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            DrawerItem(
                text = stringResource(id = R.string.nav_drawer_exit),
                textColor = MaterialTheme.colorScheme.primary,
                onClick = onExitClick
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
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun AppNavigationDrawerPreview() {
    SmartStepTheme {
        AppNavigationDrawer()
    }
}
