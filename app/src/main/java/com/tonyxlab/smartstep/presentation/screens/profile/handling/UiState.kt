package com.tonyxlab.smartstep.presentation.screens.profile.handling

import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

data class ProfileUiState(val isOkay: Boolean): UiState

enum class HeightMode {CENTIMETERS, FEET_INCHES}
enum class WeightMode {KILOS, POUNDS}