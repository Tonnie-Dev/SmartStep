package com.tonyxlab.smartstep.domain.model

import java.time.LocalDate

data class DailyMetric(
    val date: LocalDate,
    val stepCount: Int,
    val dailyStepGoal: Int,
    val calories: Int,
    val activeSeconds: Int,
    val distanceKm: Double
)
