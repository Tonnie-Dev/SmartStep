@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.toDisplayDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}

fun getTimeOfTheDay(): String {

    val hour = LocalDateTime.now().hour

    return when (hour) {
        in 0..11 -> "morning"
        in 12..17 -> "afternoon"
        else -> "evening"
    }
}