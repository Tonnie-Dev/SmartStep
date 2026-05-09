@file:RequiresApi(Build.VERSION_CODES.S)

package com.tonyxlab.smartstep.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.tonyxlab.smartstep.data.service.StepCounterService

fun Context.startStepCounterService(
    steps: Int,
    calories: Int,
    goal: Int
) {
    val intent = Intent(this, StepCounterService::class.java).apply {
        putExtra("steps", steps)
        putExtra("calories", calories)
        putExtra("goal", goal)
        putExtra("timeLabel", "now")
    }

    if (!StepCounterService.isRunning) {

        ContextCompat.startForegroundService(this, intent)
    }

}

fun Context.stopStepCounterService() {
    val intent = Intent(this, StepCounterService::class.java)
    stopService(intent)
}
