package com.tonyxlab.smartstep.di

import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.presentation.screens.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::OnboardingViewModel)
}

val dataStoreModule = module {
    singleOf(::OnboardingDataStore)
}

val appModule = listOf(viewModelModule, dataStoreModule)
