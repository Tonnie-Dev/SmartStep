package com.tonyxlab.smartstep.presentation.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeViewModel
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel
import com.tonyxlab.smartstep.R
@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    BaseContentLayout(
        viewModel = viewModel,
            topBar = {
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
    ) { uiState ->
        HomeScreenContent(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    SmartStepTheme {
        HomeScreenContent(
            uiState = HomeUiState(),
            onEvent = {}
        )
    }
}
