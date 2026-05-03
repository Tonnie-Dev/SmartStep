@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.local.database.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity
import com.tonyxlab.smartstep.domain.model.DailyMetric
import java.time.LocalDate

fun DailyMetric.toEntity() =
    DailyMetricEntity(
            date = date.toEpochDay(),
            stepCount = stepCount,
            calories = calories,
            activeMinutes = activeMinutes,
            distanceKm = distanceKm
    )

fun DailyMetricEntity.toModel() =
    DailyMetric(
            date = LocalDate.ofEpochDay(date),
            stepCount = stepCount,
            calories = calories,
            activeMinutes = activeMinutes,
            distanceKm = distanceKm
    )
