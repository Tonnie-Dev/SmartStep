package com.tonyxlab.smartstep.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SmartStepColorScheme = lightColorScheme(
    primary = ButtonPrimary,
    onPrimary = TextWhite,
    secondary = ButtonSecondary,
    onSecondary = TextPrimary,
    background = BackgroundMain,
    onBackground = TextPrimary,
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundSecondary,
    onSurfaceVariant = TextSecondary,
    outline = StrokeMain,
    tertiary = BackgroundTertiary,
    onTertiary = TextPrimary
)

@Composable
fun SmartStepTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SmartStepColorScheme,
        typography = Typography,
        content = content
    )
}
