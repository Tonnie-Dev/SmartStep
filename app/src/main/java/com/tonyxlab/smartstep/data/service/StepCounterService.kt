@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.data.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.motion.ActivityDurationTracker
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
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.scope.serviceScope
import java.time.LocalDate

class StepCounterService : Service() {



private val stepCounterManager: StepCounterManager by inject()
    private val activityStatsRepository: ActivityStatsRepository by inject()
    private val metricsRepository: MetricsRepository by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val activityDurationTracker = ActivityDurationTracker(
            activityTimeoutSeconds = ACTIVITY_TIMEOUT_IN_SECONDS.toInt()
    )

    private var currentDate: LocalDate = LocalDate.now()
    private var activeSeconds: Int = 0
    private var lastSavedActiveSeconds: Int = 0

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
            restoreTodayMetric()

            stepCounterManager.steps.collect { steps ->
                handleStepUpdate(steps)
            }
        }
    }
    private suspend fun handleStepUpdate(steps: Int) {
        checkForDateRollover()

        val addedActiveSeconds = activityDurationTracker.onStepReading(steps)

        if (addedActiveSeconds > 0) {
            activeSeconds += addedActiveSeconds
        }

        activityStatsRepository.updateStepCount(steps)

        if (shouldPersist(addedActiveSeconds)) {
            persistStepAndActiveSeconds(steps)
        }

        StepNotificationHelper.updateNotification(
                context = this@StepCounterService,
                steps = steps,
                calories = 0,
                goal = 10_000
        )
    }

    private fun shouldPersist(addedActiveSeconds: Int): Boolean {
        if (addedActiveSeconds <= 0) return false

        return activeSeconds - lastSavedActiveSeconds >= METRIC_SAVE_INTERVAL_SECONDS
    }

    private suspend fun persistStepAndActiveSeconds(steps: Int) {
        metricsRepository.upsertStepAndActiveSeconds(
                date = currentDate,
                steps = steps,
                activeSeconds = activeSeconds
        )

        lastSavedActiveSeconds = activeSeconds
    }

      private suspend fun checkForDateRollover() {
        val today = LocalDate.now()

        if (today == currentDate) return

        currentDate = today
        activeSeconds = 0
        lastSavedActiveSeconds = 0
        activityDurationTracker.reset()

        restoreTodayMetric()
    }
    private suspend fun restoreTodayMetric() {
        val todayMetric = withContext(Dispatchers.IO) {
            metricsRepository.getMetricForDate(currentDate)
        }

        activeSeconds = todayMetric?.activeSeconds ?: 0
        lastSavedActiveSeconds = activeSeconds
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        isRunning = false

        serviceScope.launch {
            val latestSteps = stepCounterManager.steps.value

            metricsRepository.upsertStepAndActiveSeconds(
                    date = currentDate,
                    steps = latestSteps,
                    activeSeconds = activeSeconds
            )
        }

        stepCounterManager.stop()
        serviceScope.cancel()

        super.onDestroy()
    }

    companion object {
        var isRunning = false
    }
}
