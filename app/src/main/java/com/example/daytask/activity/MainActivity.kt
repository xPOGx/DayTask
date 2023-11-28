package com.example.daytask.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.daytask.DayTaskApp
import com.example.daytask.R
import com.example.daytask.ui.theme.DayTaskTheme
import com.example.daytask.util.Constants.TIME_CHANGED
import com.example.daytask.util.Constants.backgroundRGB
import com.example.daytask.util.firebase.FirebaseManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val timeChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            if (action != null && action == Intent.ACTION_TIME_TICK)
                LocalBroadcastManager.getInstance(this@MainActivity)
                    .sendBroadcast(Intent(TIME_CHANGED))
        }
    }
    private var intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = backgroundRGB
        window.navigationBarColor = backgroundRGB
        checkNotifications()

        setContent {
            DayTaskTheme {
                Surface(
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DayTaskApp()
                }
            }
        }
    }

    private fun checkNotifications() {
        CoroutineScope(Dispatchers.Default).launch {
            Firebase.database.reference.child("users/${Firebase.auth.uid}/notification")
                .get().addOnCompleteListener {
                    if (it.isSuccessful && it.result.hasChildren()) {
                        Toast.makeText(
                            applicationContext,
                            applicationContext.resources.getString(R.string.unread_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(timeChangedReceiver, intentFilter)
        FirebaseManager.updateUserStatus(true)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(timeChangedReceiver)
        FirebaseManager.updateUserStatus(false)
    }
}

fun MainActivity.restartApp() {
    val intent = Intent(this, AuthActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    finish()
    Runtime.getRuntime().exit(0)
}