@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.notification.StepNotificationHelper

class StepCounterService : Service() {

    override fun onCreate() {
        super.onCreate()
        StepNotificationHelper.createChannel(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val steps = intent?.getIntExtra("steps", 0) ?: 0
        val calories = intent?.getIntExtra("calories", 0) ?: 0
        val goal = intent?.getIntExtra("goal", 10000) ?: 10000
        val timeLabel = intent?.getStringExtra("timeLabel") ?: "now"

        val notification = StepNotificationHelper.buildNotification(
                context = this,
                steps = steps,
                calories = calories,
                goal = goal,
                timeLabel = timeLabel
        )

        startForeground(StepNotificationHelper.NOTIFICATION_ID, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}