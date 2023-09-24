package com.example.daytask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.daytask.ui.theme.DayTaskTheme
import com.example.daytask.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firstTime = true
        if (firstTime) {
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            this.finish()
        } else {
            setContent {
                DayTaskTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Text(
                            text = "Main Activity",
                            color = White
                        )
                    }
                }
            }
        }
    }
}
