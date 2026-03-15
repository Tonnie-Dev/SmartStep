package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import android.provider.SyncStateContract
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.tonyxlab.smartstep.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.io.IOException




class OnboardingDataStore(private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val ONBOARDING_SKIPPED = booleanPreferencesKey("onboarding_skipped")
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
            }

    val onboardingSkipped: Flow<Boolean> = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.ONBOARDING_SKIPPED] ?: false
            }

    suspend fun saveOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun saveOnboardingSkipped(skipped: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_SKIPPED] = skipped
        }
    }
}
