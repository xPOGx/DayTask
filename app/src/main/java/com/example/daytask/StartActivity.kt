package com.example.daytask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = (this.application as DayTaskApplication)
        val repository = application.container.firstTimeRepository
        var firstTime: Boolean
        runBlocking { firstTime = repository.firstTime.first() }
        if (firstTime) {
            runBlocking { repository.saveFT(false) }
            startActivity(Intent(this, SplashActivity::class.java))
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        finish()
    }
}