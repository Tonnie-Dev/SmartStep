@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.data.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.motion.StepCounterManager
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper
import com.tonyxlab.smartstep.domain.repository.ActivityStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StepCounterService : Service() {

    private val stepCounterManager: StepCounterManager by inject()
    private val activityStats: ActivityStats by inject()

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
                activityStats.updateStepCount(steps)

                StepNotificationHelper.updateNotification(
                        context = this@StepCounterService,
                        steps = steps,
                        calories = 0,
                        goal = 10_000
                )
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        var isRunning = false
    }


    override fun onDestroy() {
        isRunning = false
        stepCounterManager.stop()
        serviceScope.cancel()
        super.onDestroy()
    }
}
