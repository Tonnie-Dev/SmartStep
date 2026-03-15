package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.Gender
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.HeightMode
import com.tonyxlab.smartstep.presentation.screens.onboarding.handling.WeightMode
import com.tonyxlab.smartstep.utils.Constants
import com.tonyxlab.smartstep.utils.Constants.DEFAULT_WEIGHT_KG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OnboardingDataStore(private val context: Context) {

    private object OnboardingKeyPreferences {
        val ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
        val SELECTED_GENDER = stringPreferencesKey("selected_gender")
        val HEIGHT_MODE = stringPreferencesKey("height_mode")
        val HEIGHT_IN_CM = intPreferencesKey("height_in_cm")
        val WEIGHT_MODE = stringPreferencesKey("weight_mode")
        val WEIGHT_IN_KG = intPreferencesKey("weight_in_kg")
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

    val selectedGender: Flow<Gender> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else throw e
    }
            .map { prefs ->
                val genderString =
                    prefs[OnboardingKeyPreferences.SELECTED_GENDER] ?: Gender.FEMALE.name
                Gender.valueOf(genderString)
            }

    suspend fun setSelectedGender(gender: Gender) {

        context.dataStore.edit { prefs ->
            prefs[OnboardingKeyPreferences.SELECTED_GENDER] = gender.name
        }
    }

    val heightMode: Flow<HeightMode> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else throw e
    }
            .map { prefs ->
                val heightModeString =
                    prefs[OnboardingKeyPreferences.HEIGHT_MODE] ?: HeightMode.CENTIMETERS.name
                HeightMode.valueOf(heightModeString)
            }

    suspend fun setHeightMode(heightMode: HeightMode) {

        context.dataStore.edit { prefs ->

            prefs[OnboardingKeyPreferences.HEIGHT_MODE] = heightMode.name
        }
    }

    val heightInCm: Flow<Int> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else throw e
    }
            .map { prefs ->
                prefs[OnboardingKeyPreferences.HEIGHT_IN_CM] ?: Constants.DEFAULT_HEIGHT_CM
            }

    suspend fun setHeightInCm(heightInCm: Int) {

        context.dataStore.edit { prefs ->
            prefs[OnboardingKeyPreferences.HEIGHT_IN_CM] = heightInCm
        }
    }

    val weightMode: Flow<WeightMode> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else throw e
    }
            .map { prefs ->
                val weightModeString =
                    prefs[OnboardingKeyPreferences.WEIGHT_MODE] ?: WeightMode.KGS.name
                WeightMode.valueOf(weightModeString)
            }

    suspend fun setWeightMode(weightMode: WeightMode) {

        context.dataStore.edit { prefs ->
            prefs[OnboardingKeyPreferences.WEIGHT_MODE] = weightMode.name
        }
    }

    val weightInKg: Flow<Int> = context.dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else throw e
    }
            .map { prefs ->
                prefs[OnboardingKeyPreferences.WEIGHT_IN_KG] ?: DEFAULT_WEIGHT_KG
            }

    suspend fun setWeightInKg(weightInKg: Int) {
        context.dataStore.edit { prefs ->
            prefs[OnboardingKeyPreferences.WEIGHT_IN_KG] = weightInKg
        }
    }
}
