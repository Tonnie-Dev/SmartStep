package com.tonyxlab.smartstep.presentation.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator(private val backStack: SnapshotStateList<NavDestination>) {

    private fun push(destination: NavDestination) {

        backStack.add(destination)
    }

    fun navigateToHome() {
        backStack.clear()
        push(HomeDestination)
    }

    fun navigateToOnboarding(){
       push(OnboardingDestination)

    }

    fun popBackStack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

}
