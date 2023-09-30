package com.example.daytask.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val FIRST_TIME_PREFERENCE_NAME = "first_time_preferences"
private val Context.dataStoreFirstTime: DataStore<Preferences> by preferencesDataStore(
    name = FIRST_TIME_PREFERENCE_NAME
)

interface AppContainer {
    val firstTimeRepository: FirstTimeRepository
}

class DefaultAppDataContainer(
    private val context: Context
) : AppContainer {
    override val firstTimeRepository: FirstTimeRepository by lazy {
        FirstTimeRepository(context.dataStoreFirstTime)
    }
}