package com.tonyxlab.smartstep.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Formatter

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toDisplayDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}