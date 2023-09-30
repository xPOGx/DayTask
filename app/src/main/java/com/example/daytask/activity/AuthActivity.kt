package com.example.daytask.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.daytask.ui.screens.auth.AuthDialog
import com.example.daytask.ui.screens.auth.AuthScreen
import com.example.daytask.ui.theme.DayTaskTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val activity = this

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val name = currentUser.displayName
            goToMainActivity(name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                    var load by remember {
                        mutableStateOf(false)
                    }
                    AuthScreen(
                        registration = { email, password, name ->
                            load = true
                            activity.registration(email, password, name) { load = false }
                        },
                        logIn = { email, password ->
                            load = true
                            activity.logIn(email, password) { load = false }
                        }
                    )
                    if (load) {
                        AuthDialog()
                    }
                }
            }
        }
    }


    private fun goToMainActivity(userName: String?) {
        val intent = Intent(this, MainActivity::class.java)
        val name = if (userName.isNullOrEmpty()) "no name" else userName
        intent.putExtra("user", name)
        startActivity(intent)
        finish()
    }

    private fun registration(
        email: String,
        password: String,
        name: String,
        change: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    createProfile(name)
                } else {
                    errorToast(task.exception!!)
                }
                change()
            }
    }

    private fun createProfile(name: String) {
        val profile = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        val user = auth.currentUser!!
        user.updateProfile(profile)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    goToMainActivity(user.displayName)
                } else {
                    errorToast(task.exception!!)
                }
            }
    }

    private fun logIn(
        email: String,
        password: String,
        change: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    goToMainActivity(user.displayName)
                } else {
                    errorToast(task.exception!!)
                }
                change()
            }
    }

    private fun errorToast(exception: Exception) {
        Toast.makeText(
            this.applicationContext,
            "Authentication failed. ${exception.message}",
            Toast.LENGTH_SHORT,
        ).show()
    }
}
