@file:SuppressLint("BatteryLife")

package com.tonyxlab.smartstep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tonyxlab.smartstep.data.service.StepCounterService

fun Activity.openAppSettings() {
    val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
    )
    startActivity(intent)
}


fun Activity.requestIgnoreBatteryOptimizations() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = "package:$packageName".toUri()
    }
    startActivity(intent)
}



fun Context.isIgnoringBatteryOptimizations(): Boolean {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    return powerManager.isIgnoringBatteryOptimizations(packageName)
}


@Composable
fun OnResumeEffect( onResume:()-> Unit){

    val lifeCycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifeCycleOwner) {

        val observer = LifecycleEventObserver { _,event ->
            if (event == Lifecycle.Event.ON_RESUME){
                onResume()
            }
        }

        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

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
    ContextCompat.startForegroundService(this, intent)
}


fun Context.stopStepCounterService() {
    val intent = Intent(this, StepCounterService::class.java)
    stopService(intent)
}