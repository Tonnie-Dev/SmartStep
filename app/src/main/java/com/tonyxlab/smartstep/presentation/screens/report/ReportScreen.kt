package com.tonyxlab.smartstep.presentation.screens.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatActionEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportScreen(
    navigator: Navigator,
    viewModel: ReportViewModel = koinViewModel()
) {

    BaseContentLayout(viewModel = viewModel,

            topBar = {
                AppTopBar(
                        titleText = stringResource(R.string.topbar_text_report),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        navigationIcon = {
                            IconButton(
                                    onClick = { navigator.navigateToHome() }) {
                                Icon(
                                        painter = painterResource(R.drawable.ic_chevron_left),
                                        contentDescription = stringResource(id = R.string.cds_open_back)
                                )
                            }
                        }
                )
            },
            actionEventHandler = { _, action ->

            }
            ) {
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
            Text(text = "Report Screen")
        }
    }
}
