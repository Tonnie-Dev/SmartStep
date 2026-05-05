package com.tonyxlab.smartstep.domain.model

import com.tonyxlab.smartstep.utils.UnitConverter
import java.time.LocalDate

data class DailyMetric(
    val date: LocalDate,
    val stepCount: Int,
    val calories: Int,
    val activeSeconds: Int,
    val distanceKm: Double
){

    val activeMinutes: Int
        get() = UnitConverter.secondsToDisplayMinutes(activeSeconds)
}
