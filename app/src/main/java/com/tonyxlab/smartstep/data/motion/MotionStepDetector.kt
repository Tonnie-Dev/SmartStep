package com.tonyxlab.smartstep.data.motion

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs
import kotlin.math.sqrt

class MotionStepDetector(
    context: Context,
    private val onStepDetected: () -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastStepTimestamp = 0L
    private var lastMagnitude = 0f

    // More emulator-friendly tuning
    private val deltaThreshold = 1.2f
    private val minStepDelayMs = 300L

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                    this,
                    it,
                    SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitude = sqrt(x * x + y * y + z * z)
        val delta = abs(magnitude - lastMagnitude)
        val now = System.currentTimeMillis()

        Log.d(
                "MotionStepDetector",
                "x=$x, y=$y, z=$z, magnitude=$magnitude, delta=$delta"
        )

        if (delta > deltaThreshold && now - lastStepTimestamp > minStepDelayMs) {
            lastStepTimestamp = now
            onStepDetected()
        }

        lastMagnitude = magnitude
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}