package com.tonyxlab.smartstep.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavDestination {
}

@Serializable
data class OnboardingDestination(val instanceId: Long = System.currentTimeMillis()) : NavDestination

@Serializable
data object HomeDestination : NavDestination


@Serializable
data object ChatDestination: NavDestination

@Serializable
data object ReportDestination: NavDestination