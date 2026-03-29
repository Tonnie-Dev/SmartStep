package com.tonyxlab.smartstep.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme

@Composable
fun CloseAppDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
                modifier = modifier
                        .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                    modifier = Modifier
                            .padding(all = MaterialTheme.spacing.spaceTwelve * 2),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_power),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
                Text(
                        text = stringResource(id = R.string.caption_text_fully_close_app),
                        style = MaterialTheme.typography.BodyLargeRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))
                Button(
                        onClick = onConfirm,
                        modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.spacing.spaceTwelve * 4),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                ) {
                    Text(
                            text = stringResource(id = R.string.button_text_ok),
                            style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CloseAppDialogPreview() {
    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            CloseAppDialog(
                    onConfirm = {},
                    onDismiss = {}
            )
        }
    }
}
