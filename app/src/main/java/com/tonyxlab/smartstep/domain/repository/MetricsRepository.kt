package com.tonyxlab.smartstep.domain.repository

import com.tonyxlab.smartstep.domain.model.DailyMetric
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MetricsRepository {

    fun getAllMetrics(): Flow<List<DailyMetric>>
    fun getWeeklyMetrics(startDate: LocalDate): Flow<List<DailyMetric>>
    suspend fun upsertDailyMetric(
        newDailyMetric: DailyMetric,
        allowDecreases: Boolean
    )
    suspend fun getMetricForDate(date: LocalDate): DailyMetric?
    fun observeMetricForDate(date: LocalDate): Flow<DailyMetric?>
    /*suspend fun upsertDailyMetric(
        newDailyMetric: DailyMetric,
        allowDecreases: Boolean
    )*/
}