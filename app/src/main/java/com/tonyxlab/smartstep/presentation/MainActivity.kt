package com.tonyxlab.smartstep.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.presentation.navigation.HomeDestination
import com.tonyxlab.smartstep.presentation.navigation.NavDestination
import com.tonyxlab.smartstep.presentation.navigation.OnboardingDestination
import com.tonyxlab.smartstep.presentation.navigation.SmartStepNavHost
import com.tonyxlab.smartstep.presentation.theme.BackgroundMain
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val onboardingDataStore: OnboardingDataStore by inject()

    var isAppReady by mutableStateOf(false)
    var startDestination by mutableStateOf<NavDestination?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            startDestination = resolveStartDestination()
            isAppReady = true
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition { !isAppReady }
        }
        enableEdgeToEdge(
                navigationBarStyle = SystemBarStyle.light(
                        scrim = BackgroundMain.toArgb(),
                        darkScrim = BackgroundMain.toArgb()
                )
        )
        setContent {
            SmartStepTheme {

                startDestination?.let { destination ->
                    SmartStepNavHost(startDestination = destination)
                }
            }
        }
    }

    private suspend fun resolveStartDestination(): NavDestination {
        val onboardingSeen = onboardingDataStore.onboardingSeen.first()
        return if (onboardingSeen) HomeDestination else OnboardingDestination
    }
}