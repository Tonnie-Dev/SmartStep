package com.tonyxlab.smartstep.presentation.screens.onboarding.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.smartstep.presentation.core.base.handling.UiState
import com.tonyxlab.smartstep.utils.Constants
import com.tonyxlab.smartstep.utils.Constants.DEFAULT_HEIGHT_CM
import com.tonyxlab.smartstep.utils.Constants.DEFAULT_WEIGHT_KG
import com.tonyxlab.smartstep.utils.Constants.DEFAULT_WEIGHT_LB

data class OnboardingUiState(
    val onboardingSeen: Boolean = false,
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
        val selectedCentimeter: Int = DEFAULT_HEIGHT_CM,
        val selectedFeet: Int = Constants.DEFAULT_HEIGHT_FT,
        val selectedInches: Int = Constants.DEFAULT_HEIGHT_IN,

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
        val weightMode: WeightMode = WeightMode.KGS,
        val selectedKgs: Int = DEFAULT_WEIGHT_KG,
        val selectedLbs: Int = DEFAULT_WEIGHT_LB
    ) {
        val displayWeight: String
            get() = when (weightMode) {
                WeightMode.KGS -> "$selectedKgs kg"
                WeightMode.LBS -> "$selectedLbs lbs"
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
enum class WeightMode { KGS, LBS }



