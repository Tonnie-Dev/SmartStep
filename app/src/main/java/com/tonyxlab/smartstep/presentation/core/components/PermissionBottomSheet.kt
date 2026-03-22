@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.presentation.core.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.components.PermissionSheetType
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.HorizontalRoundedCornerShape28
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionBottomSheet(
    isSheetVisible: Boolean,
    permissionSheetType: PermissionSheetType?,
    modifier: Modifier = Modifier,
    hasHandle: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onEvent: (HomeUiEvent) -> Unit
) {

    if (isSheetVisible) {
        ModalBottomSheet(
                onDismissRequest = { onEvent(HomeUiEvent.DismissPermissionDialog) },
                sheetState = sheetState,
                shape = MaterialTheme.shapes.HorizontalRoundedCornerShape28,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = {
                    if (hasHandle) {
                        BottomSheetDefaults.DragHandle()
                    }
                },
                modifier = modifier
        ) {

            when (permissionSheetType) {
                PermissionSheetType.INITIAL_DENIAL -> {
                    BottomSheetContentOne(
                            onAllowAccessClick = { onEvent(HomeUiEvent.AllowAccess) }
                    )

                }

                PermissionSheetType.PERMANENT_DENIAL -> {

                    BottomSheetContentTwo(
                            onOpenSettings = { onEvent(HomeUiEvent.OpenPermissionsSettings) }
                    )
                }

                PermissionSheetType.BACKGROUND_ACCESS -> {

                    BottomSheetContentThree(
                            onContinue = { onEvent(HomeUiEvent.Continue) }
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
fun BottomSheetContentOne(
    onAllowAccessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier

                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.spaceMedium)
                    .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
                modifier = Modifier.size(MaterialTheme.spacing.spaceDoubleDp * 22),
                painter = painterResource(id = R.drawable.ic_road),
                contentDescription = null
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceExtraLarge),
                text = stringResource(id = R.string.caption_text_motion_sensors),
                style = MaterialTheme.typography.BodyLargeMedium,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

        AppButton(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                        .padding(bottom = MaterialTheme.spacing.spaceMedium),
                onClick = onAllowAccessClick,
                buttonText = stringResource(id = R.string.button_text_allow_access),
        )

    }
}

@Composable
fun BottomSheetContentTwo(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier

                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.spaceMedium)
                    .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceExtraLarge),
                text = stringResource(id = R.string.caption_text_enable_access),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceDoubleDp * 22),
                text = stringResource(id = R.string.caption_text_track_step),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.BodyLargeRegular,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

        Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                horizontalAlignment = Alignment.Start
        ) {

            Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.caption_text_instruction_1),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.BodyLargeMedium,
            )

            Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.caption_text_instruction_2),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.BodyLargeMedium,
            )


            Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.caption_text_instruction_3),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.BodyLargeMedium,
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))
        AppButton(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                        .padding(bottom = MaterialTheme.spacing.spaceMedium),
                onClick = onOpenSettings,
                buttonText = stringResource(id = R.string.button_text_open_settings),
        )

    }
}

@Composable
fun BottomSheetContentThree(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
            modifier = modifier

                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.spaceMedium)
                    .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceLarge),
                text = stringResource(id = R.string.caption_text_background_access),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceDoubleDp * 22),
                text = stringResource(id = R.string.caption_text_background_access_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.BodyLargeRegular,
                textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))
        AppButton(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                        .padding(bottom = MaterialTheme.spacing.spaceMedium),
                onClick = onContinue,
                buttonText = stringResource(id = R.string.button_text_continue),
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BottomSheetContentOnePreview() {

    SmartStepTheme {
        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {
            PermissionBottomSheet(
                    isSheetVisible = true,
                    hasHandle = true,
                    permissionSheetType = PermissionSheetType.INITIAL_DENIAL,
                    onEvent = {}
            )
        }
    }
}
