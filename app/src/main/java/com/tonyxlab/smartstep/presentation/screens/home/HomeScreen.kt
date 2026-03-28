@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.home.components.AppNavigationDrawer
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.components.StepCounterCard
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.isIgnoringBatteryOptimizations
import com.tonyxlab.smartstep.utils.openAppSettings
import com.tonyxlab.smartstep.utils.rememberIsDeviceWide
import com.tonyxlab.smartstep.utils.requestIgnoreBatteryOptimizations
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeViewModel = koinViewModel()
) {
    val activity = LocalActivity.current ?: return
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var shouldOpenDrawer by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSheetVisible, shouldOpenDrawer) {
        if (shouldOpenDrawer && !uiState.isSheetVisible) {
            drawerState.open()
            shouldOpenDrawer = false
        }
    }

    ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppNavigationDrawer(
                        onEvent = { event ->
                            scope.launch { drawerState.close() }

                            when (event) {
                                HomeUiEvent.FixCountIssue -> {
                                    // TODO navigate or show screen
                                }

                                HomeUiEvent.SetStepGoal -> {
                                    // TODO navigate or show dialog
                                }

                                HomeUiEvent.OpenPersonalSettings -> {
                                    // TODO navigate
                                }

                                HomeUiEvent.ExitApp -> {
                                    activity.finish()
                                }

                                else -> Unit
                            }
                        }
                )
            }
    ) {
        BaseContentLayout(
                viewModel = viewModel,
                topBar = {
                    AppTopBar(
                            titleText = stringResource(R.string.topbar_text_smart_step),
                            navigationIcon = {
                                IconButton(
                                        onClick = {

                                            scope.launch {

                                                if (uiState.isSheetVisible) {
                                                    shouldOpenDrawer = true
                                                    viewModel.onEvent(HomeUiEvent.DismissPermissionDialog)
                                                } else {
                                                    scope.launch { drawerState.open() }
                                                }
                                            }
                                             }
                                ) {
                                    Icon(
                                            painter = painterResource(id = R.drawable.ic_menu),
                                            contentDescription = stringResource(id = R.string.cds_open_drawer)
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
                            if (!activity.isIgnoringBatteryOptimizations()) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val isDeviceWide = rememberIsDeviceWide()
    val maxWidth = if (isDeviceWide) 394.dp else Dp.Unspecified
    Box(
            modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(all = MaterialTheme.spacing.spaceMedium),
            contentAlignment = Alignment.Center
    ) {
        StepCounterCard(
                modifier = Modifier.widthIn(max = maxWidth),
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
