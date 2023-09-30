package com.example.daytask.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.daytask.DayTaskApplication
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = (this.application as DayTaskApplication)
        val context = this
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val repository = application.container.firstTimeRepository
                repository.firstTime.collectLatest { firstTime ->
                    if (firstTime) {
                        repository.saveFT(false)
                        startActivity(Intent(context, SplashActivity::class.java))
                    } else startActivity(Intent(context, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }
}