package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PermPrefsDataStore(val context: Context) {

    private data object PermissionPrefs {
        val PHYSICAL_ACTIVITY_PERMISSION_REQUESTED =
            booleanPreferencesKey("physical_activity_permission_requested")

    }

    val physicalActivityPermissionRequested: Flow<Boolean> = context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map {
                it[PermissionPrefs.PHYSICAL_ACTIVITY_PERMISSION_REQUESTED] ?: false
            }

    suspend fun setPhysicalActivityPermissionRequested(requested: Boolean) {
        context.dataStore.edit {
            it[PermissionPrefs.PHYSICAL_ACTIVITY_PERMISSION_REQUESTED] = requested
        }
    }
}