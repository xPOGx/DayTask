package com.example.daytask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.daytask.ui.screens.splashscreen.SplashScreen
import com.example.daytask.ui.theme.DayTaskTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DayTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen(
                        nextActivity = {
                            startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                            this@SplashActivity.finish()
                        }
                    )
                }
            }
        }
    }
}