package com.tonyxlab.smartstep.presentation.screens.onboarding.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState

data class OnboardingUiState(
    val genderSelectionState: GenderSelectionState = GenderSelectionState(),
    val heightPickerState: HeightPickerState = HeightPickerState(),
    val weightPickerState: WeightPickerState = WeightPickerState()
) : UiState {

    @Stable
    data class GenderSelectionState(
        val visible: Boolean = false,
        val selectedGender: Gender = Gender.FEMALE,
        val genderOptions: List<Gender> = Gender.entries,
    )

    @Stable
    data class HeightPickerState(
        val visible: Boolean = false,
        val heightMode: HeightMode = HeightMode.CENTIMETERS,
        val selectedCentimeter: Int = 175,
        val centimeterRange: IntRange = 100..250,
        val selectedFeet: Int = 5,
        val selectedInches: Int = 9,

        ) {
        val displayHeight: String
            get() = when (heightMode) {
                HeightMode.CENTIMETERS -> "$selectedCentimeter cm"
                HeightMode.FEET_INCHES -> "$selectedFeet ft $selectedInches in"
            }
    }

    @Stable
    data class WeightPickerState(
        val visible: Boolean = false,
        val weightMode: WeightMode = WeightMode.KILOS,
        val selectedKilos: Int = 60,
        val selectedPounds: Int = 143
    ) {
        val displayWeight: String
            get() = when (weightMode) {
                WeightMode.KILOS -> "$selectedKilos kg"
                WeightMode.POUNDS -> "$selectedPounds lbs"
            }
    }

}

enum class Gender {
    FEMALE, MALE;

    override fun toString(): String {
        return when (this) {
            MALE -> "Male"
            else -> "Female"
        }
    }

}

enum class HeightMode { CENTIMETERS, FEET_INCHES }
enum class WeightMode { KILOS, POUNDS }

data class OnboardingValues(
    val selectedGender: Gender,
    val heightMode: HeightMode,
    val weightMode: WeightMode,
    val selectedHeight: Int,
    val selectedFeet: Int,
    val selectedInches: Int,
    val selectedKilos: Int,
    val selectedPounds: Int
)
