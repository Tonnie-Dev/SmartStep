@file:RequiresApi(Build.VERSION_CODES.Q)

package com.tonyxlab.smartstep.di

import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.tonyxlab.smartstep.data.ai.AiClient
import com.tonyxlab.smartstep.data.ai.AiCoachImpl
import com.tonyxlab.smartstep.data.local.datastore.BaselineDataStore
import com.tonyxlab.smartstep.data.local.datastore.OnboardingDataStore
import com.tonyxlab.smartstep.data.local.datastore.PermPrefsDataStore
import com.tonyxlab.smartstep.data.motion.StepCounterManager
import com.tonyxlab.smartstep.data.remote.connectivity.ConnectivityObserverImpl
import com.tonyxlab.smartstep.data.repository.ActivityStatsImpl
import com.tonyxlab.smartstep.domain.ai.AiCoach
import com.tonyxlab.smartstep.domain.connectivity.ConnectivityObserver
import com.tonyxlab.smartstep.domain.repository.ActivityStats
import com.tonyxlab.smartstep.presentation.screens.chat.ChatViewModel
import com.tonyxlab.smartstep.presentation.screens.home.HomeViewModel
import com.tonyxlab.smartstep.presentation.screens.home.handling.AnalyticsHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.InsightHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.PermissionHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.ResetExitHandler
import com.tonyxlab.smartstep.presentation.screens.home.handling.StepsHandler
import com.tonyxlab.smartstep.presentation.screens.onboarding.OnboardingViewModel
import com.tonyxlab.smartstep.presentation.screens.report.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Dispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::ReportViewModel)
}

val repositoryModule = module {
    single<ActivityStats> { ActivityStatsImpl() }
}


val coroutineScopeModule = module {
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers .Default) }
}
val dataStoreModule = module {
    singleOf(::OnboardingDataStore)
    singleOf(::PermPrefsDataStore)
    singleOf(::BaselineDataStore)
}

val connectivityModule = module {
    single<ConnectivityManager> {
        ContextCompat.getSystemService(
                androidContext(),
                ConnectivityManager::class.java
        ) ?: error("Connectivity Manager not available")
    }

    single<ConnectivityObserver> { ConnectivityObserverImpl(get()) }
}

val stepCounterManagerModule = module {
    single<StepCounterManager> {
        StepCounterManager(
                context = androidContext(),
                baselineDataStore = get(),
                scope = get()
        )
    }
}

val aiCoachModule = module {
    singleOf(::AiClient)
    single<AiCoach> { AiCoachImpl(get()) }
}

val handlersModule = module {
    singleOf(::StepsHandler)
    singleOf(::PermissionHandler)
    singleOf(::ResetExitHandler)
    singleOf(::AnalyticsHandler)
    singleOf(::InsightHandler)
}

val appModule = listOf(
        viewModelModule,
        dataStoreModule,
        connectivityModule,
        aiCoachModule,
        repositoryModule,
        handlersModule,
        coroutineScopeModule,
        stepCounterManagerModule
)
