package com.example.daytask.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FirstTimeRepository(
    private val dataStoreFirstTime: DataStore<Preferences>
) {
    private companion object {
        val FIRST_TIME = booleanPreferencesKey("first_time")
        const val TAG = "FirstTimeRepository"
    }

    suspend fun saveFT(firstTime: Boolean) {
        dataStoreFirstTime.edit { mutablePreferences ->
            mutablePreferences[FIRST_TIME] = firstTime
        }
    }

    val firstTime: Flow<Boolean> = dataStoreFirstTime.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else throw it
        }
        .map { preferences ->
            preferences[FIRST_TIME] ?: true
        }
}