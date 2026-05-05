@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.data.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.motion.StepCounterManager
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.domain.repository.ActivityStatsRepository
import com.tonyxlab.smartstep.domain.repository.MetricsRepository
import com.tonyxlab.smartstep.utils.MeasurementConstants.ACTIVITY_TIMEOUT_IN_SECONDS
import com.tonyxlab.smartstep.utils.MeasurementConstants.METRIC_SAVE_INTERVAL_SECONDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate

class StepCounterService : Service() {

    private val stepCounterManager: StepCounterManager by inject()
    private val activityStatsRepository: ActivityStatsRepository by inject()
    private val metricsRepository: MetricsRepository by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var lastStepTimestampMillis: Long = 0L
    private var isActivityOngoing: Boolean = false
    private var activityTimerJob: Job? = null
private var accumulatedActiveSeconds:Int = 0


    companion object {
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()

        isRunning = true
        StepNotificationHelper.createChannel(this)
        startForeground(
                StepNotificationHelper.NOTIFICATION_ID,
                StepNotificationHelper.buildNotification(
                        context = this,
                        steps = 0,
                        calories = 0,
                        goal = 10_000
                )
        )

        stepCounterManager.start()
        serviceScope.launch {
            stepCounterManager.steps.collect { steps ->
                activityStatsRepository.updateStepCount(steps)

                StepNotificationHelper.updateNotification(
                        context = this@StepCounterService,
                        steps = steps,
                        calories = 0,
                        goal = 10_000
                )
            }
        }
    }
private fun onStepActivityDetected() {

    lastStepTimestampMillis = System.currentTimeMillis()

    if (!isActivityOngoing){
        isActivityOngoing = true
        startActivityTimer()
    }
}

    private fun startActivityTimer() {
        activityTimerJob?.cancel()
        val today = LocalDate.now()
        val now = System.currentTimeMillis()
        activityTimerJob = serviceScope.launch {


            // Restore today's saved active secs
            accumulatedActiveSeconds = metricsRepository.getMetricForDate(today)?.activeSeconds ?: 0

            while (isActive){
                delay(1000)

                val secondsSinceLastStep = (now -lastStepTimestampMillis)/1000

               // User is active
                if (secondsSinceLastStep<= ACTIVITY_TIMEOUT_IN_SECONDS){
                    accumulatedActiveSeconds ++

                    if (accumulatedActiveSeconds%METRIC_SAVE_INTERVAL_SECONDS == 0){

                        persistActiveSeconds()
                    }
                }
               // User is idle
                else {
                    isActivityOngoing = false
                    persistActiveSeconds()
                    cancel()

                }

            }
        }
    }

    private suspend fun persistActiveSeconds() {
        val today = LocalDate.now()
        val existing = metricsRepository.getMetricForDate(today)
        val metric = DailyMetric(
                date = today,
                stepCount = existing?.stepCount ?: 0,
                calories = existing?.calories ?: 0,
                activeSeconds = maxOf(existing?.activeSeconds ?: 0, accumulatedActiveSeconds),
                distanceKm = existing?.distanceKm ?: 0.0
        )
        metricsRepository.upsertDailyMetric(metric)
    }

    override fun onBind(intent: Intent?): IBinder? = null



    override fun onDestroy() {
        isActivityOngoing = false
        activityTimerJob?.cancel()
        serviceScope.launch { persistActiveSeconds() } // final flush on destroy
        stepCounterManager.stop()
        serviceScope.cancel()
        super.onDestroy()
    }
}
