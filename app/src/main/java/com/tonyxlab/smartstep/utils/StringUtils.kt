package com.tonyxlab.smartstep.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import java.text.NumberFormat

fun Int.formatWithCommas(): String {
    return NumberFormat.getNumberInstance()
            .format(this)
}

sealed class MetadataValue {
    data class WholeNumber(val value: Int) : MetadataValue()
    data class Decimal(val value: Double, val decimals: Int = 1) : MetadataValue()
}

fun metadataAnnotatedString(
    metadataValue: MetadataValue,
    unit: String,
    metadataValueStyle: TextStyle,
    unitStyle: TextStyle
): AnnotatedString = buildAnnotatedString {
    withStyle(style = metadataValueStyle.toSpanStyle()) {
        val formatted = when (metadataValue) {
            is MetadataValue.WholeNumber -> "${metadataValue.value}"
            is MetadataValue.Decimal -> "%.${metadataValue.decimals}f".format(metadataValue.value)
        }
        append(formatted)
    }
    append(" ")
    withStyle(style = unitStyle.toSpanStyle()) {
        append(unit)
    }
}