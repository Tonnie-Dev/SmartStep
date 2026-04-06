package com.tonyxlab.smartstep.utils

import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.utils.MeasurementConstants.CALORIES_PER_STEP
import com.tonyxlab.smartstep.utils.MeasurementConstants.CALORY_GENDER_FACTOR_FEMALE
import com.tonyxlab.smartstep.utils.MeasurementConstants.CALORY_GENDER_FACTOR_MALE
import com.tonyxlab.smartstep.utils.MeasurementConstants.CMS_PER_METER
import com.tonyxlab.smartstep.utils.MeasurementConstants.CM_PER_FOOT
import com.tonyxlab.smartstep.utils.MeasurementConstants.CM_PER_INCH
import com.tonyxlab.smartstep.utils.MeasurementConstants.INCH_PER_FOOT
import com.tonyxlab.smartstep.utils.MeasurementConstants.KG_PER_LB
import com.tonyxlab.smartstep.utils.MeasurementConstants.METERS_PER_KM
import com.tonyxlab.smartstep.utils.MeasurementConstants.METERS_PER_MILE
import com.tonyxlab.smartstep.utils.MeasurementConstants.STEP_LENGTH_FACTOR
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

object UnitConverter {
    data class FeetInches(
        val feet: Int,
        val inches: Int
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
        return (kgs * KG_PER_LB).roundToInt()
    }

    fun lbsToKgs(lbs: Int): Int {
        return (lbs / KG_PER_LB).roundToInt()
    }

    fun stepsToKm(heightInCm: Int, steps: Int): Double {
        return (stepsToMeters(heightInCm, steps) / METERS_PER_KM).roundToOneDecimal()
    }

    fun stepsToMiles(heightInCm: Int, steps: Int): Double {
        return (stepsToMeters(heightInCm, steps) / METERS_PER_MILE).roundToOneDecimal()
    }

    fun stepsToCalories(steps: Int, weight: Int, gender: Gender): Int {

        val genderFactor = when (gender) {
            Gender.MALE -> CALORY_GENDER_FACTOR_MALE
            Gender.FEMALE -> CALORY_GENDER_FACTOR_FEMALE
        }
        val kcalPerStep = weight * CALORIES_PER_STEP * genderFactor

        return (kcalPerStep * steps).roundToInt()
    }

    fun secondsToDisplayMinutes(seconds:Int): Int{
        if (seconds<60)return 0
        return (seconds/60.0).roundToInt()
    }

    private fun stepsToMeters(heightInCm: Int, steps: Int): Double {
        val stepLength = heightInCm.toDouble() * STEP_LENGTH_FACTOR
        return (stepLength * steps) / CMS_PER_METER
    }

    private fun Double.roundToOneDecimal(): Double {
        return BigDecimal(this.toString()).setScale(1, RoundingMode.HALF_UP)
                .toDouble()
    }

}








