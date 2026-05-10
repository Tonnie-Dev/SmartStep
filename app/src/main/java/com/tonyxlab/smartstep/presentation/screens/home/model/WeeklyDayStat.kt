package com.tonyxlab.smartstep.presentation.screens.home.model


data class WeeklyDayStat(
    val dayLabel: String,
    val steps: Int,
    val goalAtThatDay: Int,

    ) {

    val progress: Float
        get() = if (goalAtThatDay > 0)
            (steps.toFloat() / goalAtThatDay).coerceAtMost(1f)
        else
            0f
}
