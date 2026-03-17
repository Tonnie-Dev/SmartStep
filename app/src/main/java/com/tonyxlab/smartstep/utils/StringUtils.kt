package com.tonyxlab.smartstep.utils

import java.text.NumberFormat

fun Int. formatWithCommas(): String{
    return NumberFormat.getNumberInstance().format(this)
}