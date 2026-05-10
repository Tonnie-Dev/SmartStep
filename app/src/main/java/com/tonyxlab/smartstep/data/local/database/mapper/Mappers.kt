package com.tonyxlab.smartstep.data.local.database.mapper

import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity
import com.tonyxlab.smartstep.domain.model.DailyMetric
import java.time.LocalDate

fun DailyMetric.toEntity() =
    DailyMetricEntity(
            date = date.toEpochDay(),
            stepCount = stepCount,
            dailyStepGoal = dailyStepGoal,
            calories = calories,
            activeSeconds = activeSeconds,
            distanceKm = distanceKm
    )

fun DailyMetricEntity.toModel() =
    DailyMetric(
            date = LocalDate.ofEpochDay(date),
            stepCount = stepCount,
            dailyStepGoal = dailyStepGoal,
            calories = calories,
            activeSeconds = activeSeconds,
            distanceKm = distanceKm
    )
