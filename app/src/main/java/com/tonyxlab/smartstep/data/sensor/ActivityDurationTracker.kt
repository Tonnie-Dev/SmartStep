package com.tonyxlab.smartstep.data.sensor

import com.tonyxlab.smartstep.utils.MeasurementConstants.ACTIVITY_TIMEOUT_IN_SECONDS

class ActivityDurationTracker() {


    private val activityTimeoutSeconds = ACTIVITY_TIMEOUT_IN_SECONDS

    private var previousSteps: Int? = null
    private var lastMovementTimestampMillis: Long? = null

    fun onStepReading(steps: Int, nowMillis: Long = System.currentTimeMillis()): Int {
        val previous = previousSteps
        previousSteps = steps

        // First sensor reading is only a baseline.
        // Do not treat it as movement.
        if (previous == null) return 0

        // No increase means no movement.
        if (steps <= previous) return 0

        val lastMovementTime = lastMovementTimestampMillis
        lastMovementTimestampMillis = nowMillis

        // First real movement after baseline.
        if (lastMovementTime == null) return 1

        val secondsSinceLastStep = ((nowMillis - lastMovementTime) / 1_000)
                .toInt()
                .coerceAtLeast(1)

        return if (secondsSinceLastStep <= activityTimeoutSeconds) {
            secondsSinceLastStep
        } else {
            // User had stopped for too long, so this is a new activity burst.
            // Count only the new movement moment, not the whole inactive gap.
            1
        }
    }

    fun reset() {
        previousSteps = null
        lastMovementTimestampMillis = null
    }
}