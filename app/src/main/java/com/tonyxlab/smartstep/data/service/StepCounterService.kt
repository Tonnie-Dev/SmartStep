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

    private val metricsRepository: MetricsRepository by inject()
    private val activityStatsRepository: ActivityStatsRepository by inject()
    private val onboardingDataStore: OnboardingDataStore by inject()

    private val durationTracker: ActivityDurationTracker by inject()
    private val stepCounterManager: StepCounterManager by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var currentDate: LocalDate = LocalDate.now()
    private var activeSeconds: Int = 0
    private var lastSavedActiveSeconds: Int = 0

    override fun onCreate() {
        super.onCreate()

        isRunning = true

        StepNotificationHelper.createChannel(this)

        // Temporary notification to satisfy foreground service requirement
        startForeground(
                StepNotificationHelper.NOTIFICATION_ID,
                StepNotificationHelper.buildNotification(
                        context = this,
                        steps = 0,
                        calories = 0,
                        goal = 0
                )
        )

        stepCounterManager.start()

        serviceScope.launch {
            restoreTodayTimeMetric()

            val initialSteps = stepCounterManager.steps.value
            val (initialCalories, initialGoal) = getNotificationData(initialSteps)

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

        val addedActiveSeconds = durationTracker.onStepReading(steps)

        if (addedActiveSeconds > 0) {
            activeSeconds += addedActiveSeconds
        }

        activityStatsRepository.updateStepCount(steps)

        if (shouldPersist(addedActiveSeconds)) {
          persistCurrentMetricSnapshot(steps)
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

    private suspend fun persistCurrentMetricSnapshot(steps: Int) {
        val savedMetric = withContext(Dispatchers.IO) {
            metricsRepository.getMetricForDate(currentDate)
        }

        val heightInCm = withContext(Dispatchers.IO) {
            onboardingDataStore.heightInCm.first()
        }

        val weightInKg = withContext(Dispatchers.IO) {
            onboardingDataStore.weightInKg.first()
        }

        val selectedGender = withContext(Dispatchers.IO) {
            onboardingDataStore.selectedGender.first()
        }

        val calculatedDistanceKm =
            UnitConverter.stepsToKm(
                    steps = steps,
                    heightInCm = heightInCm
            )

        val calculatedCalories =
            UnitConverter.stepsToCalories(
                    steps = steps,
                    weightInKg = weightInKg,
                    gender = selectedGender
            )

        val metricToSave = savedMetric?.copy(
                stepCount = steps,
                dailyStepGoal = savedMetric.dailyStepGoal,
                activeSeconds = activeSeconds,
                calories = calculatedCalories,
                distanceKm = calculatedDistanceKm
        ) ?: DailyMetric(
                date = currentDate,
                stepCount = steps,
                dailyStepGoal = withContext(Dispatchers.IO) {
                    onboardingDataStore.dailyStepGoal.first()
                },
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
        durationTracker.reset()

        restoreTodayTimeMetric()
    }

    private suspend fun calculateCaloriesForSteps(steps: Int): Int {
        return UnitConverter.stepsToCalories(
                steps = steps,
                weightInKg = onboardingDataStore.weightInKg.first(),
                gender = onboardingDataStore.selectedGender.first()
        )
    }

    private suspend fun getDailyStepGoal(): Int {
        return onboardingDataStore.dailyStepGoal.first()
    }

    private suspend fun restoreTodayTimeMetric() {

        val todayMetric = withContext(Dispatchers.IO) {
            metricsRepository.getMetricForDate(currentDate)
        }

        activeSeconds = todayMetric?.activeSeconds ?: 0
        lastSavedActiveSeconds = activeSeconds
    }

    private suspend fun getNotificationData(steps: Int): Pair<Int, Int> {
        val calories = UnitConverter.stepsToCalories(
                steps = steps,
                weightInKg = onboardingDataStore.weightInKg.first(),
                gender = onboardingDataStore.selectedGender.first()
        )

        val stepGoal = onboardingDataStore.dailyStepGoal.first()
        return calories to stepGoal
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        isRunning = false

        val latestSteps = stepCounterManager.steps.value

        serviceScope.launch {
            persistCurrentMetricSnapshot(latestSteps)
            serviceScope.cancel()
        }

        stepCounterManager.stop()

        super.onDestroy()
    }

    companion object {
        var isRunning = false
    }
}
