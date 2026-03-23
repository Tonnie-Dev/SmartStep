@file:SuppressLint("BatteryLife")

package com.tonyxlab.smartstep.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.core.net.toUri

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