package com.example.daytask

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.daytask.ui.screens.auth.AuthDialog
import com.example.daytask.ui.screens.auth.AuthScreen
import com.example.daytask.ui.theme.DayTaskTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMainActivity(currentUser.email ?: "no email")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            DayTaskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var load by remember {
                        mutableStateOf(false)
                    }
                    AuthScreen { email, password ->
                        load = true
                        this@AuthActivity.registration(email, password) { load = false }
                    }
                    if (load) {
                        AuthDialog()
                    }
                }
            }
        }
    }


    private fun goToMainActivity(user: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }

    private fun registration(
        email: String,
        password: String,
        change: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    goToMainActivity("Your email: ${user.email ?: "no email"}" )
                } else {
                    Toast.makeText(
                        this.applicationContext,
                        "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                change()
            }
    }
}
