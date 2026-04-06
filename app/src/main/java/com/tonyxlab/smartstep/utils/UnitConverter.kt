package com.tonyxlab.smartstep.utils

import com.tonyxlab.smartstep.utils.Constants.CM_PER_FOOT
import com.tonyxlab.smartstep.utils.Constants.CM_PER_INCH
import com.tonyxlab.smartstep.utils.Constants.CM_TO_METERS
import com.tonyxlab.smartstep.utils.Constants.INCH_PER_FOOT
import com.tonyxlab.smartstep.utils.Constants.KG_PER_LBS
import com.tonyxlab.smartstep.utils.Constants.METERS_TO_KM
import com.tonyxlab.smartstep.utils.Constants.METERS_TO_MILES
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

object UnitConverter {
    data class FeetInches(
        val feet: Int,
        val inches: Int
    )

    data class HeightStep(
            val height: Int,
            val step: Int
    )

    fun cmToFeetInches(cm: Int): FeetInches {

        val totalInches = cm / CM_PER_INCH

        var feet = (totalInches / INCH_PER_FOOT).toInt()
        var inches = (totalInches % INCH_PER_FOOT).roundToInt()

        if (inches == INCH_PER_FOOT) {
            feet += 1
            inches = 0
        }

        return FeetInches(
                feet = feet,
                inches = inches
        )
    }

    fun feetInchesToCm(feet: Int, inches: Int): Int {
        val cm = (feet * CM_PER_FOOT) + (inches * CM_PER_INCH)
        return cm.roundToInt()
    }

    fun kgsToLbs(kgs: Int): Int {
        return (kgs * KG_PER_LBS).roundToInt()
    }

    fun lbsToKgs(lbs: Int): Int {
        return (lbs / KG_PER_LBS).roundToInt()
    }

    fun stepsToKm(heightInCm: Int, steps: Int): Double {
        return (stepsToMeters(heightInCm, steps) / METERS_TO_KM).roundToOneDecimal()
    }

    fun stepsToMiles(heightInCm: Int, steps: Int): Double {
        return (stepsToMeters(heightInCm, steps) / METERS_TO_MILES).roundToOneDecimal()
    }

    private fun stepsToMeters(heightInCm: Int, steps: Int): Double {
        val stepLength = heightInCm.toDouble() * Constants.STEP_LENGTH_FACTOR
        return (stepLength * steps) / CM_TO_METERS
    }
}

fun Double.roundToOneDecimal(): Double {
    return BigDecimal(this.toString()).setScale(1, RoundingMode.HALF_UP).toDouble()
}








