package com.example.daytask.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.daytask.DayTaskApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        manageNavigation()
    }

    private fun manageNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (auth.currentUser != null) goToActivity(MainActivity::class.java)
                else {
                    val application = (this@StartActivity.application as DayTaskApplication)
                    val repository = application.container.firstTimeRepository
                    if (repository.firstTime.first()) {
                        repository.saveFT(false)
                        goToActivity(SplashActivity::class.java)
                    } else goToActivity(AuthActivity::class.java)
                }
            }
        }
    }

    private fun <T> goToActivity(cls: Class<T>) {
        val intent = Intent(this, cls)
        startActivity(intent)
        finish()
    }
}