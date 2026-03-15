package com.tonyxlab.smartstep.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.presentation.navigation.HomeDestination
import com.tonyxlab.smartstep.presentation.navigation.NavDestination
import com.tonyxlab.smartstep.presentation.navigation.OnboardingDestination
import com.tonyxlab.smartstep.presentation.navigation.SmartStepNavHost
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    val onboardingDataStore: OnboardingDataStore by inject()

    var isAppReady = false
    var startDestination: NavDestination = OnboardingDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            startDestination = resolveStartDestination()
            isAppReady = true
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition { !isAppReady }
        }
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {
                SmartStepNavHost(startDestination = startDestination)
            }
        }
    }

    private suspend fun resolveStartDestination(): NavDestination {
        val onboardingSeen = onboardingDataStore.onboardingSeen.first()
        return if (onboardingSeen) HomeDestination else OnboardingDestination
    }
}