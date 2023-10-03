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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StartActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        manageNavigation()
    }

    private fun manageNavigation() {
        val application = (this.application as DayTaskApplication)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val currentUser = auth.currentUser
                if (currentUser != null)
                    goToActivity(MainActivity::class.java, currentUser.displayName)
                else {
                    val repository = application.container.firstTimeRepository
                    repository.firstTime.collectLatest { firstTime ->
                        if (firstTime) {
                            repository.saveFT(false)
                            goToActivity(SplashActivity::class.java)
                        } else goToActivity(AuthActivity::class.java)
                    }
                }
            }
        }
    }

    private fun <T> goToActivity(cls: Class<T>, userName: String? = null) {
        val intent = Intent(this, cls)
        if (cls == MainActivity::class.java) {
            val name = if (userName.isNullOrEmpty()) "no name" else userName
            intent.putExtra("user", name)
        }
        startActivity(intent)
        finish()
    }
}