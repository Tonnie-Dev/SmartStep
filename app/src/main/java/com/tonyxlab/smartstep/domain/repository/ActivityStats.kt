package com.tonyxlab.smartstep.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ActivityStats {
    val stepCount: StateFlow<Int>
    val dailyGoal: StateFlow<Int>
    fun updateStepCount(steps:Int)
    fun updateDailyGoal(dailyGoal: Int)
}