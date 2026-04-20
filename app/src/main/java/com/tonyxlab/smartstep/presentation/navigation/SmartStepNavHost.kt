
@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.tonyxlab.smartstep.presentation.screens.chat.ChatScreen
import com.tonyxlab.smartstep.presentation.screens.home.HomeScreen
import com.tonyxlab.smartstep.presentation.screens.onboarding.OnboardingScreen



@Composable
fun SmartStepNavHost(startDestination: NavDestination) {

    val backStack = remember(startDestination) {
        mutableStateListOf(startDestination)
    }

    val navigator = remember(backStack) { Navigator(backStack) }

    val entryProvider = entryProvider {

        entry<OnboardingDestination> {
            OnboardingScreen(navigator = navigator)
        }

        entry<HomeDestination> {
            HomeScreen(navigator = navigator)
        }

        entry< ChatDestination> {
            ChatScreen(navigator = navigator)
        }
    }

    NavDisplay(
            backStack = backStack,
            onBack = { navigator.popBackStack() },
            entryProvider = entryProvider
    )
}