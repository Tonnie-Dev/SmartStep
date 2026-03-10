package com.tonyxlab.smartstep.presentation.screens.profile.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

data class ProfileUiState(
    val genderSelectionState: GenderSelectionState = GenderSelectionState(),
    val heightPickerState: HeightPickerState = HeightPickerState()
    ) : UiState {

    @Stable
    data class GenderSelectionState(
        val selectedGender: Gender = Gender.MALE,
        val genderOptions: List<Gender> = Gender.entries,

    )

    @Stable
    data class HeightPickerState(
        val visible: Boolean = false,
        val heightMode: HeightMode = HeightMode.CENTIMETERS,
        val selectedCentimeter: Int = 175,
        val centimeterRange: IntRange = 100..250,
        val selectedFeet: Int = 5,
        val feetRange: IntRange = 3..8,
        val selectedInches: Int = 9,
        val inchesRange: IntRange = 0..11
    ) {
        val displayHeight: String
            get() = when (heightMode) {
                HeightMode.CENTIMETERS -> "$selectedCentimeter cm"
                HeightMode.FEET_INCHES -> "$selectedFeet ft $selectedInches in"
            }
    }

}

enum class Gender {
    MALE, FEMALE;

    override fun toString(): String {
        return when (this) {
            MALE -> "Male"
            else -> "Female"
        }
    }

}



enum class HeightMode { CENTIMETERS, FEET_INCHES }
enum class WeightMode { KILOS, POUNDS }