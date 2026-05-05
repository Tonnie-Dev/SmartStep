package com.tonyxlab.smartstep.domain.repository

import androidx.room.Query
import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity
import com.tonyxlab.smartstep.domain.model.DailyMetric
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MetricsRepository {

    fun getAllMetrics(): Flow<List<DailyMetric>>
    fun getWeeklyMetrics(startDate: LocalDate): Flow<List<DailyMetric>>
    suspend fun upsertDailyMetric(dailyMetric: DailyMetric)
    suspend fun getMetricForDate(date: LocalDate): DailyMetric?
    fun observeMetricForDate(date: LocalDate): Flow<DailyMetric?>
}