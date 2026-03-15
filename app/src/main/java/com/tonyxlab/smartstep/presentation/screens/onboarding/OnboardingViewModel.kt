package com.tonyxlab.smartstep.presentation.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.HeightMode
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingActionEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiState
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.WeightMode
import kotlinx.coroutines.launch
import timber.log.Timber

typealias OnboardingBaseViewModel = BaseViewModel<OnboardingUiState, OnboardingUiEvent, OnboardingActionEvent>

class OnboardingViewModel(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingBaseViewModel() {

    override val initialState: OnboardingUiState
        get() = OnboardingUiState()

    override fun onEvent(event: OnboardingUiEvent) {
        when (event) {

            OnboardingUiEvent.StartOnboarding -> onStartOnboarding()
            OnboardingUiEvent.SkipOnboarding -> onSkipOnboarding()
            OnboardingUiEvent.HeightPickerVisibilityChange -> onHeightPickerVisibilityChange()
            OnboardingUiEvent.WeightPickerVisibilityChange -> onWeightPickerVisibilityChange()
            is OnboardingUiEvent.SelectGender -> onSelectGender(event.gender)
            is OnboardingUiEvent.SelectHeightMode -> onSelectHeightMode(event.heightMode)
            is OnboardingUiEvent.OnCentimetersSelected -> onCentimetersSelected(event.value)
            is OnboardingUiEvent.OnFeetSelected -> onFeetSelected(event.value)
            is OnboardingUiEvent.OnInchesSelected -> onInchesSelected(event.value)
            is OnboardingUiEvent.SelectWeightMode -> onSelectWeightMode(event.weightMode)
            is OnboardingUiEvent.OnKilosSelected -> onKilosSelected(event.value)
            is OnboardingUiEvent.OnPoundsSelected -> onPoundsSelected(event.value)
            OnboardingUiEvent.ConfirmHeightDialog -> onConfirmHeightDialog()
            OnboardingUiEvent.CancelHeightDialog -> onCancelHeightDialog()
            OnboardingUiEvent.ConfirmWeightDialog -> onConfirmWeightDialog()
            OnboardingUiEvent.CancelWeightDialog -> onCancelWeightDialog()

        }
    }

    private fun onStartOnboarding() {
        viewModelScope.launch {
            onboardingDataStore.saveOnboardingCompleted(true)
            onboardingDataStore.saveOnboardingSkipped(false)
        }
    }

    private fun onSkipOnboarding() {
        viewModelScope.launch {
            onboardingDataStore.saveOnboardingCompleted(false)
            onboardingDataStore.saveOnboardingSkipped(true)
        }
    }

    private fun onHeightPickerVisibilityChange() {
        Timber.tag("OnboardingVM").i("Visibility change")
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
        Timber.tag("OnboardingVM").i("Height clicked: $heightMode")
        updateState {
            it.copy(heightPickerState = it.heightPickerState.copy(heightMode = heightMode))
        }
    }

    private fun onCentimetersSelected(value: Int) {
        updateState {
            it.copy(heightPickerState = it.heightPickerState.copy(selectedCentimeter = value))
        }
    }

    private fun onFeetSelected(value: Int) {
        updateState {
            it.copy(
                heightPickerState = it.heightPickerState.copy(selectedFeet = value)
            )
        }
    }

    private fun onInchesSelected(value: Int) {
        updateState {
            it.copy(heightPickerState = it.heightPickerState.copy(selectedInches = value))
        }
    }

    private fun onSelectWeightMode(weightMode: WeightMode) {
        updateState {
            it.copy(weightPickerState = it.weightPickerState.copy(weightMode = weightMode))
        }
    }

    private fun onKilosSelected(value: Int) {
        updateState {
            it.copy(weightPickerState = it.weightPickerState.copy(selectedKilos = value))
        }
    }

    private fun onPoundsSelected(value: Int) {
        updateState {
            it.copy(weightPickerState = it.weightPickerState.copy(selectedPounds = value))
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
}
