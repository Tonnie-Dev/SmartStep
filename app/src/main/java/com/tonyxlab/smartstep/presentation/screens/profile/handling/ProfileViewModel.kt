package com.tonyxlab.smartstep.presentation.screens.profile.handling

import com.tonyxlab.smartstep.presentation.core.base.BaseViewModel

typealias ProfileBaseViewModel = BaseViewModel<ProfileUiState, ProfileUiEvent, ProfileActionEvent>

class ProfileViewModel() : ProfileBaseViewModel() {

    override val initialState: ProfileUiState
        get() = ProfileUiState()

    override fun onEvent(event: ProfileUiEvent) {
        when (event) {

            ProfileUiEvent.SkipOnboarding -> {}
            ProfileUiEvent.HeightPickerVisibilityChange -> onHeightPickerVisibilityChange()
            ProfileUiEvent.WeightPickerVisibilityChange -> onWeightPickerVisibilityChange()
            is ProfileUiEvent.SelectGender -> onSelectGender(event.gender)
            is ProfileUiEvent.SelectHeightMode -> onSelectHeightMode(event.heightMode)
            is ProfileUiEvent.OnCentimetersSelected -> onCentimetersSelected(event.value)
            is ProfileUiEvent.OnFeetSelected -> onFeetSelected(event.value)
            is ProfileUiEvent.OnInchesSelected -> onInchesSelected(event.value)
            is ProfileUiEvent.SelectWeightMode -> onSelectWeightMode(event.weightMode)
            is ProfileUiEvent.OnKilosSelected -> onKilosSelected(event.value)
            is ProfileUiEvent.OnPoundsSelected -> onPoundsSelected(event.value)
            ProfileUiEvent.ConfirmHeightDialog -> onConfirmHeightDialog()
            ProfileUiEvent.CancelHeightDialog -> onCancelHeightDialog()
            ProfileUiEvent.ConfirmWeightDialog -> onConfirmWeightDialog()
            ProfileUiEvent.CancelWeightDialog -> onCancelWeightDialog()

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
        // TODO: Save to database/prefs if needed
    }

    private fun onCancelHeightDialog() {
        updateState { it.copy(heightPickerState = it.heightPickerState.copy(visible = false)) }
    }

    private fun onConfirmWeightDialog() {
        updateState { it.copy(weightPickerState = it.weightPickerState.copy(visible = false)) }
        // TODO: Save to database/prefs if needed
    }

    private fun onCancelWeightDialog() {
        updateState { it.copy(weightPickerState = it.weightPickerState.copy(visible = false)) }
    }
}

