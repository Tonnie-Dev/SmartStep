package com.tonyxlab.smartstep.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tonyxlab.smartstep.R

private val InterFontFamily = FontFamily(
    Font(R.font.inter_18_regular, weight = FontWeight.Normal),
    Font(R.font.inter_18_medium, weight = FontWeight.Medium),
    Font(R.font.inter_variable_font, weight = FontWeight.SemiBold)
)


val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),

    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
)

object ExtendedTypography {

        val TitleAccent = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 64.sp,
        lineHeight = 70.sp
        )

        val BodyLargeRegular = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp
        )

       val BodyLargeMedium = TextStyle(
               fontFamily = InterFontFamily,
               fontWeight = FontWeight.Medium,
               fontSize = 16.sp,
               lineHeight = 24.sp
       )

        val BodyMediumRegular = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 18.sp
        )

        val BodyMediumMedium = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp
        )
        val BodySmallRegular = TextStyle(
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp
        )
}


val Typography.TittleAccent
        @Composable
        get() = ExtendedTypography. TitleAccent

val Typography.BodyLargeRegular
        @Composable
        get() = ExtendedTypography. BodyLargeRegular

val Typography.BodyLargeMedium
        @Composable
        get() = ExtendedTypography. BodyLargeMedium

val Typography.BodyMediumRegular
       @Composable
        get() = ExtendedTypography. BodyMediumRegular

val Typography.BodyMediumMedium
        @Composable
        get() = ExtendedTypography. BodyMediumMedium

val Typography.BodySmallRegular
        @Composable
        get() = ExtendedTypography. BodySmallRegular
