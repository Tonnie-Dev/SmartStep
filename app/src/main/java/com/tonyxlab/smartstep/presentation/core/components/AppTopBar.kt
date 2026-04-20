package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titleText: String,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
    navigationIcon: @Composable (() -> Unit)? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                navigationIcon?.invoke()
            },
            actions = {
                if (actionText != null) {
                    Text(
                            text = actionText,
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                    .padding(end = MaterialTheme.spacing.spaceMedium)
                                    .clickable { onActionClick() }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = background
            )
    )
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    SmartStepTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            AppTopBar(
                    titleText = stringResource(id = R.string.my_profile),
                    actionText = stringResource(id = R.string.skip)
            )
            AppTopBar(
                    titleText = stringResource(R.string.topbar_text_smart_step),
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = "Menu"
                            )
                        }
                    }
            )

        }
    }
}
