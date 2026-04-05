package com.tonyxlab.smartstep.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.components.AppDialog
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.screens.home.handling.HomeUiEvent
import com.tonyxlab.smartstep.presentation.theme.BodyLargeMedium
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun ResetStepsDialog(
    onEvent:(HomeUiEvent)-> Unit,
    modifier: Modifier = Modifier
) {
    AppDialog(
            modifier = modifier,
            onDismiss = { onEvent(HomeUiEvent.DismissResetDialog)}
    ) {
        Column(
                modifier = Modifier
                        .padding(
                                top = MaterialTheme.spacing.spaceTwelve * 3,
                                bottom = MaterialTheme.spacing.spaceTwelve,
                                start = MaterialTheme.spacing.spaceTwelve * 2,
                                end = MaterialTheme.spacing.spaceTwelve * 2
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceMedium),
                    text = stringResource(id = R.string.dialog_text_reset),
                    style = MaterialTheme.typography.BodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {onEvent(HomeUiEvent.DismissResetDialog)}) {
                    Text(
                            text = stringResource(id = R.string.button_text_cancel),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                        onClick = { onEvent(HomeUiEvent.ConfirmResetDialog)}
                ) {
                    Text(
                            text = stringResource(id = R.string.text_button_reset),
                            style = MaterialTheme.typography.BodyLargeMedium,
                            color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ResetStepsDialogPreview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            ResetStepsDialog(onEvent = {})
        }
    }
}
