package com.tonyxlab.smartstep.data.repository

import com.tonyxlab.smartstep.data.local.database.dao.MetricsDao
import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity
import com.tonyxlab.smartstep.data.local.database.mapper.toEntity
import com.tonyxlab.smartstep.data.local.database.mapper.toModel
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.domain.repository.MetricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import kotlin.math.max

class MetricsRepositoryImpl(private val dao: MetricsDao) : MetricsRepository {
    override fun getAllMetrics(): Flow<List<DailyMetric>> {

        return dao.getAllMetrics()
                .map { entities -> entities.map(DailyMetricEntity::toModel) }
    }

    override fun getWeeklyMetrics(startDate: LocalDate): Flow<List<DailyMetric>> {
        val start = startDate.toEpochDay()
        val end = startDate.plusDays(6)
                .toEpochDay()

        return dao.getWeeklyMetrics(startDate = start, endDate = end)
                .map { entities -> entities.map(DailyMetricEntity::toModel) }
    }

/*    override suspend fun upsertDailyMetric(dailyMetric: DailyMetric) {
        dao.upsertDailyMetric(dailyMetric.toEntity())
    }*/

    override suspend fun getMetricForDate(date: LocalDate): DailyMetric? {
        return dao.getMetricForDate(date.toEpochDay())
                ?.toModel()
    }

    override fun observeMetricForDate(date: LocalDate): Flow<DailyMetric?> {
        return dao.observeMetricForDate(date = date.toEpochDay())
                .map { entity -> entity?.toModel() }
    }

    override suspend fun upsertDailyMetric(
        newDailyMetric: DailyMetric,
        allowDecreases: Boolean
    ) {
        val savedMetric = dao.getMetricForDate(newDailyMetric.date.toEpochDay())
        val metricToSave = if (savedMetric == null || allowDecreases) {
            newDailyMetric
        } else {
            newDailyMetric.copy(
                    stepCount = maxOf(savedMetric.stepCount, newDailyMetric.stepCount),
                    activeSeconds = max(savedMetric.activeSeconds, newDailyMetric.activeSeconds),
                    calories = newDailyMetric.calories,
                    distanceKm = newDailyMetric.distanceKm
            )
        }

        dao.upsertDailyMetric(metricToSave.toEntity())
    }
}