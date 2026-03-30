
@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.presentation.screens.home.HomeViewModel
import com.tonyxlab.smartstep.presentation.screens.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
}

val dataStoreModule = module {
    singleOf(::OnboardingDataStore)
    singleOf(::PermPrefsDataStore)
}

@RequiresApi(Build.VERSION_CODES.Q)
val appModule = listOf(viewModelModule, dataStoreModule)
