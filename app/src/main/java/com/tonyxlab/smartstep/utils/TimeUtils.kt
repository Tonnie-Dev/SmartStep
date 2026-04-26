@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

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


object WeekUtils{

    private val weekFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH)

    fun getFirstDayOfTheWeek(date:LocalDate = LocalDate.now()): LocalDate{
        return date.with (TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    fun getLastDayOfTheWeek(weekStartDate: LocalDate ): LocalDate{
        return weekStartDate.plusDays(6)
    }

    fun formatWeekRange(weekStartDate: LocalDate): String {
       val weekLastDay = getLastDayOfTheWeek(weekStartDate)
       return "${weekStartDate.format(weekFormatter)} - ${weekLastDay.format(weekFormatter)}"

    }

    fun isCurrentWeek(selectedWeekStartDate: LocalDate): Boolean{
        return selectedWeekStartDate == getFirstDayOfTheWeek()
    }

}