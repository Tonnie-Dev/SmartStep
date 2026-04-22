package com.tonyxlab.smartstep.data.repository

import com.tonyxlab.smartstep.domain.repository.ActivityStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActivityStatsImpl(

) : ActivityStats{

    private val _stepCount = MutableStateFlow(0)
    override val stepCount: StateFlow<Int>
        get() = _stepCount.asStateFlow()

    private val _dailyGoal = MutableStateFlow(2000)
    override val dailyGoal: StateFlow<Int>
        get() = _dailyGoal.asStateFlow()



    override fun updateStepCount(steps: Int) {
       _stepCount.value = steps


    }

    override fun updateDailyGoal(dailyGoal: Int) {
        _dailyGoal.value = dailyGoal
    }

}