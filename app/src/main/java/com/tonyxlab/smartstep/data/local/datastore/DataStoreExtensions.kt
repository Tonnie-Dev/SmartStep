package com.tonyxlab.smartstep.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

const val DATASTORE_NAME = "smart_step_datastore"

val Context.dataStore by preferencesDataStore(
        name = DATASTORE_NAME
)
