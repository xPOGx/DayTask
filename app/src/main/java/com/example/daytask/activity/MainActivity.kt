package com.example.daytask.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.daytask.ui.DayTaskApp
import com.example.daytask.ui.theme.DayTaskTheme
import com.example.daytask.util.Constants.TIME_CHANGED

class MainActivity : ComponentActivity() {
    private val timeChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            if (action != null && action == Intent.ACTION_TIME_TICK) LocalBroadcastManager
                .getInstance(this@MainActivity)
                .sendBroadcast(Intent(TIME_CHANGED))
        }
    }
    private var intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        registerReceiver(timeChangedReceiver, intentFilter)

        setContent {
            DayTaskTheme {
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .imePadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DayTaskApp(
                        navigateToAuth = { goBackToAuth() }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangedReceiver)
    }

    private fun goBackToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
