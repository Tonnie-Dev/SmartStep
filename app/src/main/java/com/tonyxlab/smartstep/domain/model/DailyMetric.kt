package com.tonyxlab.smartstep.domain.model

import java.time.LocalDate

data class DailyMetric(
    val date: LocalDate,
    val stepCount: Int,
    val calories: Int,
    val activeMinutes: Int,
    val distanceKm: Double
)
