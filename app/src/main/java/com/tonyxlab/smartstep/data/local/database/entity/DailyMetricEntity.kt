package com.tonyxlab.smartstep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_metrics_table")
data class DailyMetricEntity(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "step_count")
    val stepCount: Int,
    @ColumnInfo(name = "calories")
    val calories: Int,
    @ColumnInfo(name = "active_minutes")
    val activeMinutes: Int,
    @ColumnInfo(name = "distance_km")
    val distanceKm: Double
)