@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.presentation.screens.report

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.report.components.BottomMetricTabs
import com.tonyxlab.smartstep.presentation.screens.report.components.SummaryCard
import com.tonyxlab.smartstep.presentation.screens.report.components.WeekSelector
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiEvent
import com.tonyxlab.smartstep.presentation.screens.report.handling.ReportUiState
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportScreen(
    navigator: Navigator,
    viewModel: ReportViewModel = koinViewModel()
) {
    BaseContentLayout(
            viewModel = viewModel,
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
    ) { uiState ->

        ReportScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ReportScreenContent(
    uiState: ReportUiState,
    onEvent: (ReportUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Column(
                modifier = modifier
                        .fillMaxSize()
                        .padding(all = MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            SummaryCard(uiState = uiState)

            WeekSelector(
                    uiState = uiState,
                    onEvent = onEvent
            )

        }

        BottomMetricTabs(
                modifier = Modifier.align(alignment = Alignment.BottomCenter),
                uiState = uiState,
                onEvent = onEvent
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryCard_Preview() {
    SmartStepTheme {
        ReportScreenContent(
                uiState = ReportUiState(),
                onEvent = {}
        )
    }
}
