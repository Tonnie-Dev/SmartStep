@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.tonyxlab.smartstep.data.local.datastore.BaselineDataStore.BaselineKeyPrefs.BASELINE_DATE
import com.tonyxlab.smartstep.data.local.datastore.BaselineDataStore.BaselineKeyPrefs.BASELINE_STEPS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okio.IOException
import java.time.LocalDate

class BaselineDataStore(private val context: Context) {

    private object BaselineKeyPrefs {
        val BASELINE_STEPS = floatPreferencesKey("baseline_steps")
        val BASELINE_DATE = longPreferencesKey("baseline_date")
    }

    suspend fun getBaseline(): Pair<Float, Long> {

        val prefs = context.dataStore.data
                .catch { e ->
                    if (e is IOException) emit(emptyPreferences()) else throw e

                }
                .first()

        return Pair(
                first = prefs[BASELINE_STEPS] ?: 0f,
                  second = prefs[BASELINE_DATE] ?: 0L
        )
    }


    suspend fun saveBaseline(steps: Float, date: LocalDate = LocalDate.now()){

        context.dataStore.edit { prefs->

            prefs [BASELINE_STEPS] = steps
            prefs [BASELINE_DATE] = date.toEpochDay()
        }


    }

    val baselineDate: Flow<Long> = context.dataStore.data.catch { e ->

        if (e is IOException) {
            emit(emptyPreferences())
        } else {
            throw e
        }
    }
            .map { prefs ->

                prefs[BaselineKeyPrefs.BASELINE_DATE] ?: 0L
            }

    val baselineStepCount: Flow<Float> =
        context.dataStore.data.catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else throw e
        }
                .map { prefs ->

                    prefs[BaselineKeyPrefs.BASELINE_STEPS] ?: 0f
                }

    suspend fun setBaselineStepCount(steps: Float, date: LocalDate = LocalDate.now()) {
        context.dataStore.edit { prefs ->
            prefs[BaselineKeyPrefs.BASELINE_STEPS] = steps
            prefs[BaselineKeyPrefs.BASELINE_DATE] = date.toEpochDay()
        }
    }

    suspend fun clearBaselineStepCount() {
        context.dataStore.edit { prefs ->
            prefs.remove(BaselineKeyPrefs.BASELINE_STEPS)
        }
    }
}