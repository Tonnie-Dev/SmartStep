package com.tonyxlab.smartstep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tonyxlab.smartstep.presentation.screens.onboarding.OnboardingScreen
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition { false }
        }
        enableEdgeToEdge()
        setContent {
            SmartStepTheme {

                Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                ) {

                    OnboardingScreen()
                }

            }
        }
    }

}