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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.daytask.ui.screens.auth.AuthScreen
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.ui.theme.DayTaskTheme
import com.example.daytask.util.Constants.WEB_CLIENT_ID
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleResult: ActivityResultLauncher<IntentSenderRequest>

    private var load by mutableStateOf(false)
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
                    AuthScreen(
                        signUp = this::signUp,
                        logIn = this::logIn,
                        googleSignIn = this::googleSignIn
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
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        userToDatabase()
                                        goToMainActivity()
                                    } else errorToast(task.exception!!)
                                }
                        } else errorToast(Exception("Unknown error")) /* Shouldn't happen */
                    } catch (e: ApiException) {
                        errorToast(e)
                    }
                } else errorToast(Exception("Cancelled"))
            }
    }

    private fun signUp(
        email: String,
        password: String,
        name: String
    ) {
        load = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) createProfile(name)
                else errorToast(task.exception!!)
            }
    }

    private fun logIn(
        email: String,
        password: String
    ) {
        load = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) goToMainActivity()
                else errorToast(task.exception!!)
            }
    }

    private fun googleSignIn() {
        load = true
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    val intent = IntentSenderRequest.Builder(result.pendingIntent).build()
                    googleResult.launch(intent)
                } catch (e: Exception) {
                    errorToast(e)
                }
            }
            .addOnFailureListener { errorToast(it) }
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

        auth.currentUser!!.updateProfile(profile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userToDatabase()
                    goToMainActivity()
                } else errorToast(task.exception!!)
            }
    }

    private fun errorToast(exception: Exception) {
        Toast.makeText(
            applicationContext,
            "Authentication failed. ${exception.message}",
            Toast.LENGTH_SHORT,
        ).show()
        load = false
    }

    private fun userToDatabase() {
        val user = auth.currentUser!!
        Firebase.database.reference.child("users/${user.uid}")
            .setValue(
                mapOf(
                    "displayName" to user.displayName,
                    "photoUrl" to user.photoUrl
                )
            )
    }
}
