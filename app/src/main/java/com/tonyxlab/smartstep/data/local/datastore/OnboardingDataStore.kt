package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OnboardingDataStore(private val context: Context) {

    private object OnboardingKeyPreferences {
        val ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
    }

    val onboardingSeen: Flow<Boolean> =
        context.dataStore.data.catch { e ->

            if (e is IOException) {
                emit(emptyPreferences())
            } else throw e
        }
                .map { prefs ->

                    prefs[OnboardingKeyPreferences.ONBOARDING_SEEN] ?: false
                }

    suspend fun setOnboardingSeen() {
        context.dataStore.edit { prefs ->
            prefs[OnboardingKeyPreferences.ONBOARDING_SEEN] = true
        }
    }
}
