package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import com.tonyxlab.smartstep.R
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyMediumMedium
import com.tonyxlab.smartstep.presentation.theme.EndVerticalRoundedCornerShape100
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.presentation.theme.StartVerticalRoundedCornerShape100
import com.tonyxlab.smartstep.utils.clickableWithoutRipple
import com.tonyxlab.smartstep.utils.ifThen

@Composable
fun AppLabel(
    labelText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.BodyMediumMedium,
    height: Dp = MaterialTheme.spacing.spaceTen * 4,
    shape: Shape = MaterialTheme.shapes.StartVerticalRoundedCornerShape100,
) {

    Box(
            modifier = modifier
                    .clip(shape)
                    .ifThen(selected) {
                        background(shape = shape, color = MaterialTheme.colorScheme.secondary)
                    }
                    .border(
                            width = MaterialTheme.spacing.spaceSingleDp,
                            shape = shape,
                            color = MaterialTheme.colorScheme.outline
                    )
                    .clickableWithoutRipple(onClick)
               //     .clickable{onClick()}
                    .height(height = height),
            contentAlignment = Alignment.Center

    )

    {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {

            if (selected) {
                Icon(
                        modifier = Modifier.padding(
                                end = MaterialTheme.spacing.spaceSmall
                        ),
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(id = R.string.cds_text_check_mark)
                )
            }

            Text(
                    text = labelText,
                    style = textStyle.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }

}

@PreviewLightDark
@Composable
private fun Preview() {

    SmartStepTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceExtraLarge),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceLarge)
        ) {

            AppLabel(
                    labelText = "Label",
                    selected = true,
                    onClick = {}
            )
            AppLabel(
                    labelText = "Label",
                    selected = false,
                    onClick = {}
            )
        }
    }
}


