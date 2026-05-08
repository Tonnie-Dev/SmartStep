@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.data.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.motion.ActivityDurationTracker
import com.tonyxlab.smartstep.data.motion.StepCounterManager
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper
import com.tonyxlab.smartstep.domain.model.DailyMetric
import com.tonyxlab.smartstep.domain.repository.ActivityStatsRepository
import com.tonyxlab.smartstep.domain.repository.MetricsRepository
import com.tonyxlab.smartstep.utils.MeasurementConstants.ACTIVITY_TIMEOUT_IN_SECONDS
import com.tonyxlab.smartstep.utils.MeasurementConstants.METRIC_SAVE_INTERVAL_SECONDS
import com.tonyxlab.smartstep.utils.UnitConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.time.LocalDate

class StepCounterService() : Service() {
    private val stepCounterManager: StepCounterManager by inject()
    private val activityStatsRepository: ActivityStatsRepository by inject()
    private val metricsRepository: MetricsRepository by inject()
    private val onboardingDataStore: OnboardingDataStore by inject()
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

        // Temporary notification to satisfy foreground service requirement immediately
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

            val initialSteps = stepCounterManager.steps.value
            val (initialCalories, initialGoal) = getCalculatedNotificationData(initialSteps)

            StepNotificationHelper.updateNotification(
                    context = this@StepCounterService,
                    steps = initialSteps,
                    calories = initialCalories,
                    goal = initialGoal
            )

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
                calories = calculateCaloriesForSteps(steps),
                goal = getDailyStepGoal()
        )
    }

    private fun shouldPersist(addedActiveSeconds: Int): Boolean {
        if (addedActiveSeconds <= 0) return false

        return activeSeconds - lastSavedActiveSeconds >= METRIC_SAVE_INTERVAL_SECONDS
    }

    private suspend fun persistStepAndActiveSeconds(steps: Int) {
        val savedMetric = withContext(Dispatchers.IO) {
            metricsRepository.getMetricForDate(currentDate)
        }

        val calculatedDistanceKm =
            UnitConverter.stepsToKm(steps, onboardingDataStore.heightInCm.first())

        val calculatedCalories =
            UnitConverter.stepsToCalories(
                    steps,
                    onboardingDataStore.weightInKg.first(),
                    onboardingDataStore.selectedGender.first()
            )
        val metricToSave = savedMetric?.copy(
                stepCount = steps,
                activeSeconds = activeSeconds,
                calories = calculatedCalories,
                distanceKm = calculatedDistanceKm
        ) ?: DailyMetric(
                date = currentDate,
                stepCount = steps,
                calories = calculatedCalories,
                activeSeconds = activeSeconds,
                distanceKm = calculatedDistanceKm
        )

        metricsRepository.upsertDailyMetric(
                newDailyMetric = metricToSave,
                allowDecreases = false
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
    private suspend fun calculateCaloriesForSteps(steps: Int): Int {
        return UnitConverter.stepsToCalories(
                steps = steps,
                weight = onboardingDataStore.weightInKg.first(),
                gender = onboardingDataStore.selectedGender.first()
        )
    }

    private suspend fun getDailyStepGoal(): Int {
        return onboardingDataStore.dailyStepGoal.first()
    }
    private suspend fun restoreTodayMetric() {
        val todayMetric = withContext(Dispatchers.IO) {
            metricsRepository.getMetricForDate(currentDate)
        }

        activeSeconds = todayMetric?.activeSeconds ?: 0
        lastSavedActiveSeconds = activeSeconds
    }

    private suspend fun getCalculatedNotificationData(steps: Int): Pair<Int, Int> {
        val calculatedCalories = UnitConverter.stepsToCalories(
                steps = steps,
                weight = onboardingDataStore.weightInKg.first(),
                gender = onboardingDataStore.selectedGender.first()
        )

        val stepGoal = onboardingDataStore.dailyStepGoal.first()

        return calculatedCalories to stepGoal
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        isRunning = false

        val latestSteps = stepCounterManager.steps.value

        serviceScope.launch {
            persistStepAndActiveSeconds(latestSteps)
            serviceScope.cancel()
        }

        stepCounterManager.stop()

        super.onDestroy()
    }

    companion object {
        var isRunning = false
    }
}
