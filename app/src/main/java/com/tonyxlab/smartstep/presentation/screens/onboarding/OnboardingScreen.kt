package com.tonyxlab.smartstep.presentation.screens.onboarding

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppButton
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.navigation.Navigator
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.GenderSelectionField
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.HeightPicker
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.OnboardingSelectionField
import com.tonyxlab.smartstep.presentation.screens.onboarding.components.WeightPicker
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingActionEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiEvent
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.OnboardingUiState
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape14
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.rememberIsDeviceWide
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    navigator: Navigator,
    viewModel: OnboardingViewModel = koinViewModel()
) {

    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_my_profile),
                        actionText = stringResource(id = R.string.text_button_skip),
                        onActionClick = { viewModel.onEvent(OnboardingUiEvent.SkipOnboarding) }
                )
            },
            actionEventHandler = { _, event ->
                when (event) {
                    OnboardingActionEvent.NavigateToHome -> navigator.navigateToHome()
                }
            }
    ) { uiState ->

        OnboardingScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun OnboardingScreenContent(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val isDeviceWide = rememberIsDeviceWide()

    val maxWidth = if (isDeviceWide) 394.dp else Dp.Unspecified
    Box(
            modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceLarge),
            contentAlignment = Alignment.TopCenter
    ) {
        Column(
                modifier = Modifier.widthIn(max = maxWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            Text(
                    text = stringResource(id = R.string.caption_text_profile_desc),
                    style = MaterialTheme.typography.BodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
            )

            Surface(
                    modifier = Modifier.border(
                            width = MaterialTheme.spacing.spaceSingleDp,
                            shape = MaterialTheme.shapes.RoundedCornerShape14,
                            color = MaterialTheme.colorScheme.outline
                    ),
                    shape = MaterialTheme.shapes.RoundedCornerShape14
            ) {

                Column(
                        modifier = Modifier.padding(all = MaterialTheme.spacing.spaceMedium),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                ) {

                    // Gender Field
                    with(uiState.genderSelectionState) {
                        GenderSelectionField(
                                label = stringResource(id = R.string.label_text_gender),
                                selectedGender = this.selectedGender,
                                options = this.genderOptions,
                                onSelectOption = { onEvent(OnboardingUiEvent.SelectGender(it)) },
                        )
                    }

                    // Height Picker
                    with(uiState.heightPickerState) {

                        OnboardingSelectionField(
                                label = stringResource(id = R.string.label_text_height),
                                value = displayHeight,
                                onClick = { onEvent(OnboardingUiEvent.HeightPickerVisibilityChange) }
                        )
                        if (this.visible) {
                            HeightPicker(
                                    modifier = Modifier,
                                    selectedCentimeter = this.selectedCentimeter,
                                    selectedFeet = this.selectedFeet,
                                    selectedInches = this.selectedInches,
                                    onEvent = onEvent,
                                    heightMode = this.heightMode
                            )
                        }
                    }

                    // Weight Picker
                    with(uiState.weightPickerState) {

                        OnboardingSelectionField(
                                label = stringResource(id = R.string.label_text_weight),
                                value = displayWeight,
                                onClick = { onEvent(OnboardingUiEvent.WeightPickerVisibilityChange) }
                        )
                        if (this.visible) {
                            WeightPicker(
                                    modifier = Modifier,
                                    selectedKilos = this.selectedKgs,
                                    selectedPounds = this.selectedLbs,
                                    onEvent = onEvent,
                                    weightMode = this.weightMode
                            )
                        }
                    }

                }
            }
        }


        AppButton(
                modifier = Modifier.align(Alignment.BottomCenter).widthIn(max= maxWidth),
                onClick = { onEvent(OnboardingUiEvent.CompleteOnBoarding) },
                buttonText = stringResource(id = R.string.button_text_start)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    SmartStepTheme {
        OnboardingScreenContent(
                uiState = OnboardingUiState(),
                onEvent = {},
                modifier = Modifier.fillMaxSize()
        )
    }
}
