package com.example.daytask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class StartActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstTime = true
        if (firstTime) {
            startActivity(Intent(this, SplashActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}