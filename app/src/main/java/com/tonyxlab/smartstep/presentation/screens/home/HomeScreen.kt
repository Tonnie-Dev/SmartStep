@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.core.components.PermissionBottomSheet
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.components.StepCounterCard
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.isIgnoringBatteryOptimizations
import com.tonyxlab.smartstep.utils.openAppSettings
import com.tonyxlab.smartstep.utils.requestIgnoreBatteryOptimizations
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeViewModel = koinViewModel()
) {

    val activity = LocalActivity.current ?: return
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
            },
            actionEventHandler = { _, action ->
                when (action) {
                    HomeActionEvent.OpenAppSettings -> {
                        activity.openAppSettings()

                    }

                    HomeActionEvent.RequestBatteryOptimization -> {

                        if (!activity.isIgnoringBatteryOptimizations()){

                            activity.requestIgnoreBatteryOptimizations()
                        }
                    }
                }

            }
    ) { uiState ->
        HomeScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(all = MaterialTheme.spacing.spaceMedium),
            contentAlignment = Alignment.Center
    ) {
        StepCounterCard(
                currentSteps = 7000,
                stepsGoal = 10000
        )

        PermissionHandler(
                uiState = uiState,
                onEvent = onEvent
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
