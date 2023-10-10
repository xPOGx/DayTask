package com.example.daytask.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.daytask.ui.screens.auth.AuthScreen
import com.example.daytask.ui.screens.tools.Constants.WEB_CLIENT_ID
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.ui.theme.DayTaskTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleResult : ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        auth = Firebase.auth
        initGoogle()

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
                        signUp = { email, password, name ->
                            load = true
                            signUp(email, password, name) { load = false }
                        },
                        logIn = { email, password ->
                            load = true
                            logIn(email, password) { load = false }
                        },
                        googleSignIn = {
                            load = true
                            googleSignIn { load = false }
                        }
                    )
                    if (load) {
                        LoadingDialog()
                    }
                }
            }
        }
    }

    private fun initGoogle() {
        oneTapClient = Identity
            .getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
        googleResult =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data
                    try {
                        val credential = oneTapClient.getSignInCredentialFromIntent(data)
                        val idToken = credential.googleIdToken
                        if (idToken != null) {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential (firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) goToMainActivity()
                                    else errorToast(task.exception!!)
                                }
                        } else { /* Shouldn't happen */ }
                    } catch (e: ApiException) {
                        errorToast(e)
                    }
                }
            }
    }

    private fun signUp(
        email: String,
        password: String,
        name: String,
        change: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) createProfile(name)
                else {
                    errorToast(task.exception!!)
                    change()
                }
            }
    }

    private fun logIn(
        email: String,
        password: String,
        change: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) goToMainActivity()
                else {
                    errorToast(task.exception!!)
                    change()
                }
            }
    }

    private fun googleSignIn(
        change: () -> Unit
    ) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intent = IntentSenderRequest.Builder(result.pendingIntent).build()
                    googleResult.launch(intent)
                } catch (e: Exception) {
                    errorToast(e)
                }
            }
            .addOnFailureListener(this) { e ->
                errorToast(e)
                change()
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createProfile(name: String) {
        val profile = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        val user = auth.currentUser!!
        user.updateProfile(profile)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) goToMainActivity()
                else errorToast(task.exception!!)
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
