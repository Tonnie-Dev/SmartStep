package com.tonyxlab.smartstep.presentation.screens.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportScreen(
    navigator: Navigator,
    viewModel: ReportViewModel = koinViewModel()
) {

    BaseContentLayout(viewModel = viewModel) {
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
            Text(text = "Report Screen")
        }
    }
}
