@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.screens.home

import android.app.Activity
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.data.local.motion.MotionStepDetector
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.home.components.CloseAppDialog
import com.tonyxlab.smartstep.presentation.screens.home.components.DateSelector
import com.tonyxlab.smartstep.presentation.screens.home.components.NavigationDrawer
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionUiHandler
import com.tonyxlab.smartstep.presentation.screens.home.components.ResetStepsDialog
import com.tonyxlab.smartstep.presentation.screens.home.components.StepCounterCard
import com.tonyxlab.smartstep.presentation.screens.home.components.StepGoalPicker
import com.tonyxlab.smartstep.presentation.screens.home.components.StepsEditor
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeActionEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.OnResumeEffect
import com.tonyxlab.smartstep.utils.isIgnoringBatteryOptimizations
import com.tonyxlab.smartstep.utils.openAppSettings
import com.tonyxlab.smartstep.utils.rememberIsDeviceWide
import com.tonyxlab.smartstep.utils.requestIgnoreBatteryOptimizations
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

    ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                        uiState = uiState,
                        onEvent = { event ->
                            scope.launch { drawerState.close() }

                            when (event) {
                                HomeUiEvent.FixCountIssue -> {
                                    viewModel.onEvent(event)
                                }

                                HomeUiEvent.ShowStepGoalSheet -> {
                                    viewModel.onEvent(HomeUiEvent.ShowStepGoalSheet)
                                }

                                HomeUiEvent.OpenPersonalSettings -> {
                                    navigator.navigateToOnboarding()
                                }

                                HomeUiEvent.EditSteps -> {
                                    viewModel.onEvent(HomeUiEvent.EditSteps)
                                }

                                HomeUiEvent.ResetSteps -> {
                                    viewModel.onEvent(event)
                                }

                                HomeUiEvent.ShowExitDialog -> {
                                    viewModel.onEvent(event)
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
                                                drawerState.open()
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

                        HomeActionEvent.CloseApp -> {
                            activity.exitSmartStep()
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

    val activity = LocalActivity.current ?: return
    val isDeviceWide = rememberIsDeviceWide()
    val maxWidth1 = if (isDeviceWide) 394.dp else Dp.Unspecified
    val maxWidth2 = if (isDeviceWide) 312.dp else Dp.Unspecified

    OnResumeEffect {
        val isBackgroundAccessGranted = activity.isIgnoringBatteryOptimizations()
        onEvent(HomeUiEvent.BackgroundAccessChanged(isBackgroundAccessGranted))
    }
    val context = LocalContext.current

    DisposableEffect(uiState.stepEditorState.paused) {
        val detector = MotionStepDetector(context) {
            onEvent(HomeUiEvent.OnMotionDetected)
        }

        if (!uiState.stepEditorState.paused) {
            detector.start()
        }

        onDispose {
            detector.stop()
        }
    }
    Box(
            modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(all = MaterialTheme.spacing.spaceMedium),
            contentAlignment = Alignment.Center
    ) {
        StepCounterCard(
                modifier = Modifier.widthIn(max = maxWidth1),
                uiState = uiState,
                onEvent = onEvent
        )

        PermissionUiHandler(
                isDeviceWide = isDeviceWide,
                uiState = uiState,
                onEvent = onEvent
        )

        if (uiState.stepGoalSheetState.pickerSheetVisible) {
            StepGoalPicker(
                    modifier = Modifier.widthIn(max = maxWidth2),
                    isDeviceWide = isDeviceWide,
                    selectedStep = uiState.stepGoalSheetState.selectedStepsGoal,
                    onEvent = onEvent
            )
        }

        if (uiState.stepEditorState.isStepEditorVisible) {
            StepsEditor(
                    uiState = uiState,
                    onEvent = onEvent
            )

            if (uiState.dateSelectorState.isDateSelectorVisible) {
                DateSelector(
                        selectedDay = uiState.dateSelectorState.day,
                        selectedMonth = uiState.dateSelectorState.month,
                        selectedYear = uiState.dateSelectorState.year,
                        onEvent = onEvent
                )
            }
        }

        if (uiState.showResetDialog){
            ResetStepsDialog(
                    onEvent = onEvent
            )
        }

        if (uiState.showExitDialog){
            CloseAppDialog (onEvent = onEvent)
        }
    }
}

private fun Activity.exitSmartStep() {
    /*    stopService(Intent(this, StepCounterService::class.java))

        WorkManager.getInstance(applicationContext)
                .cancelAllWorkByTag("step_tracking")*/

    finishAndRemoveTask()
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
