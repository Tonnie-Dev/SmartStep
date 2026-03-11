package com.tonyxlab.smartstep.di

import com.tonyxlab.smartstep.presentation.screens.profile.handling.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ProfileViewModel)
}

val appModule = listOf(viewModelModule)
