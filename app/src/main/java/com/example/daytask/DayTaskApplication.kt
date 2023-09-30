package com.example.daytask

import android.app.Application
import com.example.daytask.data.AppContainer
import com.example.daytask.data.DefaultAppDataContainer


class DayTaskApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppDataContainer(this)
    }
}