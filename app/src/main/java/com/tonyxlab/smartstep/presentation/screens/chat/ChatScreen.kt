package com.tonyxlab.smartstep.presentation.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.chat.components.ChatWindow
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatActionEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiEvent
import com.tonyxlab.smartstep.presentation.screens.chat.handling.ChatUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = koinViewModel()
) {

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            topBar = {
                AppTopBar(
                        titleText = stringResource(R.string.topbar_text_ai_coach),
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        navigationIcon = {

                            IconButton(
                                    onClick = { viewModel.onEvent(event = ChatUiEvent.ExitChat) }) {
                                Icon(
                                        painter = painterResource(R.drawable.ic_chevron_left),
                                        contentDescription = "Menu"
                                )
                            }

                        }
                )
            },
            actionEventHandler = { _, action ->
                when (action) {

                    ChatActionEvent.NavigateToHome -> navigator.navigateToHome()
                }

            }
    ) { uiState ->

        ChatScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )

    }

}

@Composable
fun ChatScreenContent(
    uiState: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxSize()
    ) {

        ChatWindow(
                modifier = Modifier,
                uiState = uiState,
                onEvent = onEvent
        )
    }
}

@Preview
@Composable
private fun ChatScreen_Preview() {
    SmartStepTheme {
        Column {
            ChatScreenContent(
                    uiState = ChatUiState(),
                    onEvent = {}
            )
        }
    }
}
