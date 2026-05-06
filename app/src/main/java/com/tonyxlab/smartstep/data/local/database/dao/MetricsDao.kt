package com.tonyxlab.smartstep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity
import com.tonyxlab.smartstep.domain.model.DailyMetric
import kotlinx.coroutines.flow.Flow

@Dao
interface MetricsDao{


    @Upsert
    suspend fun upsertDailyMetric(dailyMetric: DailyMetricEntity)

    @Query("SELECT * FROM daily_metrics_table ORDER BY date DESC")
    fun getAllMetrics(): Flow<List<DailyMetricEntity>>

   @Query("""
       SELECT * FROM daily_metrics_table
       WHERE date BETWEEN
       :startDate AND :endDate
       ORDER BY date ASC
   """)
    fun getWeeklyMetrics(startDate: Long, endDate:Long): Flow<List<DailyMetricEntity>>


    @Query("SELECT * FROM daily_metrics_table WHERE date = :date LIMIT 1")
    fun observeMetricForDate(date: Long): Flow<DailyMetricEntity?>
    @Query("SELECT * FROM daily_metrics_table WHERE date =:date LIMIT 1")
    fun getMetricForDate(date: Long): DailyMetricEntity?


    @Query(
            """
        INSERT INTO daily_metrics_table(
            date,
            step_count,
            calories,
            active_seconds,
            distance_km
        )
        VALUES(
            :date,
            :steps,
            0,
            :activeSeconds,
            0.0
        )
        ON CONFLICT(date) DO UPDATE SET
            step_count = MAX(step_count, excluded.step_count),
            active_seconds = MAX(active_seconds, excluded.active_seconds)
        """
    )
    suspend fun upsertStepAndActiveSeconds(
        date: Long,
        steps: Int,
        activeSeconds: Int
    )
}

