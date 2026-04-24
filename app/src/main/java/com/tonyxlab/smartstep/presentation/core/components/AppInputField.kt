package com.tonyxlab.smartstep.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.tonyxlab.smartstep.presentation.core.utils.spacing
import com.tonyxlab.smartstep.presentation.theme.BodyLargeRegular
import com.tonyxlab.smartstep.presentation.theme.RoundedCornerShape10
import com.tonyxlab.smartstep.presentation.theme.SmartStepTheme
import com.tonyxlab.smartstep.utils.clickableWithoutRipple
import com.tonyxlab.smartstep.utils.ifThen

@Composable
fun AppInputField(
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier,
    placeholderText: String = "0",
    textStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    placeholderTextStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
    height: Dp = MaterialTheme.spacing.spaceExtraSmall * 7,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    isChatInputField: Boolean = false
) {
    var focused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val chatButtonModifier = Modifier
            .clip(shape = MaterialTheme.shapes.RoundedCornerShape10)
            .border(
                    width = MaterialTheme.spacing.spaceSingleDp,
                    color = if (focused) MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline,
                    shape = MaterialTheme.shapes.RoundedCornerShape10
            )
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = MaterialTheme.spacing.spaceMedium)
            .padding(vertical = MaterialTheme.spacing.spaceTen)
    Box(
            modifier = modifier
                    .ifThen(isChatInputField) {
                        chatButtonModifier
                    }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .defaultMinSize(minHeight = height)
                    .background(color = backgroundColor)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focused = it.isFocused }
                    .clickableWithoutRipple {
                        focusRequester.requestFocus()
                    },
            contentAlignment = Alignment.Center
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

        ) {
            leadingIcon?.invoke()

            BasicTextField(
                    modifier = Modifier

                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.spaceExtraSmall),
                    state = textFieldState,
                    textStyle = textStyle.copy(
                            color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = keyboardOptions,
                    lineLimits = lineLimits,
                    decorator = { innerTextField ->
                        TextDecorator(
                                isEmpty = textFieldState.text.isEmpty(),
                                innerTextField = innerTextField,
                                focused = focused,
                                placeholderText = placeholderText,
                                placeholderTextStyle = placeholderTextStyle.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        )
                    }
            )
        }
    }
}

@Composable
fun TextDecorator(
    isEmpty: Boolean,
    focused: Boolean,
    placeholderText: String,
    placeholderTextStyle: TextStyle,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxWidth()
    ) {

        if (isEmpty && !focused) {
            Text(
                    text = placeholderText,
                    style = placeholderTextStyle
            )
        } else {
            innerTextField()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepsEditorPreview() {

    val textFieldState = remember { TextFieldState(initialText = "0") }

    SmartStepTheme {
        Box(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                contentAlignment = Alignment.Center
        ) {
            AppInputField(textFieldState = textFieldState)
        }
    }
}
