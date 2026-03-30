package com.tonyxlab.smartstep.presentation.screens.onboarding

import android.R.attr.height
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.HeightMode
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingActionEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiState
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.WeightMode
import com.tonyxlab.smartstep.utils.UnitConverter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import timber.log.Timber

typealias OnboardingBaseViewModel = BaseViewModel<OnboardingUiState, OnboardingUiEvent, OnboardingActionEvent>

class OnboardingViewModel(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingBaseViewModel() {

    override val initialState: OnboardingUiState
        get() = OnboardingUiState()

    init {
        Timber.tag("OnboardingVM").i("Init Called")
        readOnboardingStatus()
        loadPersonalSettings()
    }

    override fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.CompleteOnBoarding -> onCompleteOnboarding()
            OnboardingUiEvent.SkipOnboarding -> onSkipOnboarding()
            OnboardingUiEvent.HeightPickerVisibilityChange -> onHeightPickerVisibilityChange()
            OnboardingUiEvent.WeightPickerVisibilityChange -> onWeightPickerVisibilityChange()
            is OnboardingUiEvent.SelectGender -> onSelectGender(event.gender)
            is OnboardingUiEvent.SelectHeightMode -> onSelectHeightMode(event.heightMode)
            is OnboardingUiEvent.OnCentimetersSelected -> onCentimetersSelected(event.value)
            is OnboardingUiEvent.OnFeetSelected -> onFeetSelected(event.value)
            is OnboardingUiEvent.OnInchesSelected -> onInchesSelected(event.value)
            is OnboardingUiEvent.SelectWeightMode -> onSelectWeightMode(event.weightMode)
            is OnboardingUiEvent.OnKilosSelected -> onKgsSelected(event.value)
            is OnboardingUiEvent.OnPoundsSelected -> onPoundsSelected(event.value)
            OnboardingUiEvent.ConfirmHeightDialog -> onConfirmHeightDialog()
            OnboardingUiEvent.CancelHeightDialog -> onCancelHeightDialog()
            OnboardingUiEvent.ConfirmWeightDialog -> onConfirmWeightDialog()
            OnboardingUiEvent.CancelWeightDialog -> onCancelWeightDialog()
        }
    }

    private fun readOnboardingStatus() {
        launch {

            onboardingDataStore.onboardingSeen.collect { seen ->

                updateState { it.copy(onboardingSeen = seen) }
            }

        }
    }

    private fun onCompleteOnboarding() {
        launch {

            onboardingDataStore.setOnboardingSeen()

            onboardingDataStore.setSelectedGender(
                    gender = currentState.genderSelectionState.selectedGender
            )
            onboardingDataStore.setHeightMode(
                    heightMode = currentState.heightPickerState.heightMode
            )
            onboardingDataStore.setHeightInCm(
                    heightInCm = currentState.heightPickerState.selectedCentimeter
            )
            onboardingDataStore.setWeightMode(
                    weightMode = currentState.weightPickerState.weightMode
            )
            onboardingDataStore.setWeightInKg(
                    weightInKg = currentState.weightPickerState.selectedKgs
            )
            sendActionEvent(OnboardingActionEvent.NavigateToHome)
        }
    }

    private fun loadPersonalSettings() {

launch {

    combine(
            onboardingDataStore.selectedGender,
            onboardingDataStore.heightInCm,
            onboardingDataStore.heightMode,
            onboardingDataStore.weightInKg,
            onboardingDataStore.weightMode,

            ) { gender, height, heightMode, weight, weightMode ->

        updateState { state ->
            state.copy(

                    genderSelectionState = state.genderSelectionState.copy(
                            selectedGender = gender
                    ),
                    heightPickerState = state.heightPickerState.copy(
                            selectedCentimeter = height,
                            heightMode = heightMode
                    ),
                    weightPickerState = state.weightPickerState.copy(
                            selectedKgs = weight,
                            weightMode = weightMode
                    ),
            )
        }

    }.collect()


}



    }

    private fun onSkipOnboarding() {
        launch {
            onboardingDataStore.setOnboardingSeen()
            sendActionEvent(OnboardingActionEvent.NavigateToHome)
        }
    }

    private fun onHeightPickerVisibilityChange() {
        updateState {
            it.copy(
                    heightPickerState = it.heightPickerState.copy(
                            visible = !it.heightPickerState.visible
                    )
            )
        }
    }

    private fun onWeightPickerVisibilityChange() {
        updateState {
            it.copy(
                    weightPickerState = it.weightPickerState.copy(
                            visible = !it.weightPickerState.visible
                    )
            )
        }
    }

    private fun onSelectGender(gender: Gender) {
        updateState {
            it.copy(
                    genderSelectionState = it.genderSelectionState.copy(
                            selectedGender = gender,
                            visible = false
                    )
            )
        }
    }

    private fun onSelectHeightMode(heightMode: HeightMode) {

        updateState { state ->

            val heightState = state.heightPickerState

            when (heightMode) {

                HeightMode.FEET_INCHES -> {

                    val feetInches =
                        UnitConverter.cmToFeetInches(heightState.selectedCentimeter)

                    state.copy(
                            heightPickerState = heightState.copy(
                                    heightMode = heightMode,
                                    selectedFeet = feetInches.feet,
                                    selectedInches = feetInches.inches
                            )
                    )
                }

                HeightMode.CENTIMETERS -> {

                    val cm = UnitConverter.feetInchesToCm(
                            heightState.selectedFeet,
                            heightState.selectedInches
                    )

                    state.copy(
                            heightPickerState = heightState.copy(
                                    heightMode = heightMode,
                                    selectedCentimeter = cm
                            )
                    )
                }
            }
        }
    }

    private fun onCentimetersSelected(cms: Int) {
        updateState { state ->
            val current = state.heightPickerState
            val updatedFeetInches = UnitConverter.cmToFeetInches(cms)
            state.copy(
                    heightPickerState = current.copy(
                            selectedCentimeter = cms,
                            selectedFeet = updatedFeetInches.feet,
                            selectedInches = updatedFeetInches.inches
                    )
            )
        }
    }

    private fun onFeetSelected(value: Int) {
        updateState { state ->

            val current = state.heightPickerState
            val updatedCm =
                UnitConverter.feetInchesToCm(feet = value, inches = current.selectedInches)
            state.copy(
                    heightPickerState = current.copy(
                            selectedFeet = value,
                            selectedCentimeter = updatedCm
                    )
            )
        }
    }

    private fun onInchesSelected(value: Int) {
        updateState { state ->
            val current = state.heightPickerState
            val updatedCm = UnitConverter.feetInchesToCm(current.selectedFeet, value)

            state.copy(
                    heightPickerState = current.copy(
                            selectedInches = value,
                            selectedCentimeter = updatedCm
                    )
            )
        }
    }

    private fun onSelectWeightMode(weightMode: WeightMode) {
        updateState { state ->
            val weightState = state.weightPickerState

            when (weightMode) {
                WeightMode.KGS -> {
                    val kgs = UnitConverter.lbsToKgs(weightState.selectedLbs)
                    state.copy(
                            weightPickerState = weightState.copy(
                                    weightMode = weightMode,
                                    selectedKgs = kgs
                            )
                    )
                }

                WeightMode.LBS -> {
                    val lbs = UnitConverter.kgsToLbs(weightState.selectedKgs)

                    state.copy(
                            weightPickerState = weightState.copy(
                                    weightMode = weightMode,
                                    selectedLbs = lbs
                            )
                    )
                }
            }
        }
    }

    private fun onKgsSelected(kgs: Int) {
        updateState { state ->
            val current = state.weightPickerState
            val updatedLbs = UnitConverter.kgsToLbs(kgs)
            state.copy(
                    weightPickerState = current.copy(
                            selectedKgs = kgs,
                            selectedLbs = updatedLbs
                    )
            )
        }
    }

    private fun onPoundsSelected(value: Int) {
        updateState { state ->
            val current = state.weightPickerState
            val updatedKgs = UnitConverter.lbsToKgs(value)

            state.copy(
                    weightPickerState = current.copy(
                            selectedLbs = value,
                            selectedKgs = updatedKgs
                    )
            )
        }
    }

    private fun onConfirmHeightDialog() {
        updateState { it.copy(heightPickerState = it.heightPickerState.copy(visible = false)) }
    }

    private fun onCancelHeightDialog() {
        updateState { it.copy(heightPickerState = it.heightPickerState.copy(visible = false)) }
    }

    private fun onConfirmWeightDialog() {
        updateState { it.copy(weightPickerState = it.weightPickerState.copy(visible = false)) }
    }

    private fun onCancelWeightDialog() {
        updateState { it.copy(weightPickerState = it.weightPickerState.copy(visible = false)) }
    }

    override fun onCleared() {
        Timber.tag("OnboardingVM").i("VM Cleared")
    }
}
