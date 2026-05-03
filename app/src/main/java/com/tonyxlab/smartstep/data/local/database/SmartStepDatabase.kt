package com.tonyxlab.smartstep.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tonyxlab.smartstep.data.local.database.dao.MetricsDao
import com.tonyxlab.smartstep.data.local.database.entity.DailyMetricEntity

@Database(entities = [DailyMetricEntity::class], version = 1, exportSchema = false)
abstract class SmartStepDatabase: RoomDatabase(){
    abstract val metricsDao: MetricsDao
}