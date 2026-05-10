@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.motion

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.datastore.BaselineDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class StepCounterManager(
    context: Context,
    private val baselineDataStore: BaselineDataStore,
    private val scope: CoroutineScope,

    ) : SensorEventListener {

    private val sensorManager =
        context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepSensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private var baselineSteps: Float? = null

    private var manualStepOffset: Int = 0

    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()

    private var latestSensorStepsValue: Float? = null

    fun isSensorAvailable(): Boolean = stepSensor != null

    fun start() {
        val sensor = stepSensor ?: return
        sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_UI
        )
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensorCurrentStepsTotal = event.values[0]
        latestSensorStepsValue = sensorCurrentStepsTotal
        createBaseline(sensorCurrentStepsTotal)
        updateSteps(sensorCurrentStepsTotal)

    }

    private fun createBaseline(sensorCurrentStepsTotal: Float) {
        if (baselineSteps!= null) return

            val today = LocalDate.now()
            scope.launch {
                val (savedSteps, savedEpochDay) = baselineDataStore.getBaseline()


                    baselineSteps = if (savedEpochDay == today.toEpochDay() && savedSteps > 0) {
                        savedSteps
                    } else {
                        sensorCurrentStepsTotal.also {
                            baselineDataStore.setBaselineStepCount(
                                    steps = sensorCurrentStepsTotal,
                                    date = today
                            )
                        }
                    }

        }
    }

    private fun updateSteps(totalSteps: Float) {
        val todaySteps = (totalSteps - (baselineSteps ?: totalSteps))
                .toInt()
                .coerceAtLeast(0)

        _steps.value = todaySteps + manualStepOffset
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    suspend fun editSteps(steps: Int, date: LocalDate) {

        val today = LocalDate.now()

        if (date != today) return

        val currentSensorValue = latestSensorStepsValue ?: return
        baselineSteps = currentSensorValue

        manualStepOffset = steps

        baselineDataStore.setBaselineStepCount(
                steps = currentSensorValue,
                date = date
        )
        _steps.value = steps

    }

    suspend fun resetSteps() {
        val currentSensorValue = latestSensorStepsValue ?: return

        baselineSteps = currentSensorValue
        manualStepOffset = 0

        baselineDataStore.setBaselineStepCount(
                steps = currentSensorValue,
                date = LocalDate.now()
        )

        _steps.value = 0

    }
}