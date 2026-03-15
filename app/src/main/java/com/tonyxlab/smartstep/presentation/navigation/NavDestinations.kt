package com.tonyxlab.smartstep.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestination {
}

@Serializable
data object OnboardingDestination : NavDestination

@Serializable
data object HomeDestination : NavDestination


