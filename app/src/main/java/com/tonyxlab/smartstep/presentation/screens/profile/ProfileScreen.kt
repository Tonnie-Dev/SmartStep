package com.tonyxlab.smartstep.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.base.BaseContentLayout
import com.tonyxlab.smartstep.presentation.core.components.AppTopBar
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.profile.components.GenderSelectionField
import com.tonyxlab.smartstep.presentation.screens.profile.components.HeightPicker
import com.tonyxlab.smartstep.presentation.screens.profile.components.ProfileSelectionField
import com.tonyxlab.smartstep.presentation.screens.profile.components.WeightPicker
import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileUiEvent
import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileUiState
import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileViewModel
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape14
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {

    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_my_profile),
                        actionText = stringResource(id = R.string.text_button_skip),
                        onActionClick = { viewModel.onEvent(ProfileUiEvent.SkipOnboarding) }
                )
            }
    ) { uiState ->

        ProfileScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceLarge)
    ) {
        Column(
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
                                onClickGenderSelection = { onEvent(ProfileUiEvent.GenderSelectionVisibilityChange) },
                                onSelectOption = { onEvent(ProfileUiEvent.SelectGender(it)) },
                        )
                    }

                    // Height Picker
                    with(uiState.heightPickerState) {

                        ProfileSelectionField(
                                label = stringResource(id = R.string.label_text_height),
                                value = displayHeight,
                                onClick = { onEvent(ProfileUiEvent.HeightPickerVisibilityChange) }
                        )
                        if (this.visible) {
                            HeightPicker(
                                    modifier = Modifier,
                                    onEvent = onEvent
                            )
                        }
                    }

                    // Weight Picker
                    with(uiState.weightPickerState) {

                        ProfileSelectionField(
                                label = stringResource(id = R.string.label_text_weight),
                                value = displayWeight,
                                onClick = { onEvent(ProfileUiEvent.WeightPickerVisibilityChange) }
                        )
                        if (this.visible) {
                            WeightPicker(
                                    modifier = Modifier,
                                    onEvent = onEvent
                            )
                        }
                    }

                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    SmartStepTheme {
        ProfileScreenContent(
                uiState = ProfileUiState(),
                onEvent = {},
                modifier = Modifier.fillMaxSize()
        )
    }
}
