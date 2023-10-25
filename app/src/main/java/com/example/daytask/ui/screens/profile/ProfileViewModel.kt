package com.example.daytask.ui.screens.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.daytask.activity.AuthActivity
import com.example.daytask.activity.MainActivity
import com.example.daytask.ui.screens.tools.FirebaseManager
import com.example.daytask.util.Constants
import com.example.daytask.util.NetworkManager.isNetworkAvailable
import com.example.daytask.util.NotifyManager.notifyUser
import com.example.daytask.util.Status
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.ByteArrayOutputStream
import java.util.UUID


class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    val disabled = Firebase.auth.currentUser!!.providerData
        .asSequence().map { it.providerId }.contains("google.com")

    fun updateUserName(context: Context) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        updateStatus(Status.Loading)

        Firebase.auth.currentUser!!.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(_uiState.value.userName)
                .build()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser!!
                    FirebaseManager.updateUserName(user.uid, user.displayName)
                    updateUiState(_uiState.value.copy(updateResult = true))
                }
                else updateStatus(Status.Done)
                notifyUser(task, context)
            }
    }

    fun updateUserAvatar(context: Context, bitmap: Bitmap) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        updateStatus(Status.Loading)

        val uuid = UUID.randomUUID().toString()
        val imageRef = Firebase.storage.reference
            .child("users/${_uiState.value.user.uid}/images/$uuid")

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()
        val metadata = StorageMetadata.Builder()
            .setContentType("image")
            .build()

        imageRef.putBytes(imageData, metadata)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Firebase.auth.currentUser!!.updateProfile(
                                    UserProfileChangeRequest.Builder()
                                        .setPhotoUri(task2.result)
                                        .build()
                                )
                                    .addOnCompleteListener { task3 ->
                                        if (task.isSuccessful) {
                                            val user = Firebase.auth.currentUser!!
                                            FirebaseManager.updateUserPhoto(user.uid, user.photoUrl.toString())
                                            updateUiState(_uiState.value.copy(updateResult = true))
                                        }
                                        else updateStatus(Status.Done)
                                        notifyUser(task3, context)
                                    }
                            } else {
                                notifyUser(task2, context)
                                updateStatus(Status.Done)
                            }
                        }
                } else {
                    notifyUser(task, context)
                    updateStatus(Status.Done)
                }
            }

    }

    fun updateUserEmail(context: Context) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        updateStatus(Status.Loading)

        val password = _uiState.value.userPassword
        val email = Firebase.auth.currentUser!!.email!!
        val newEmail = _uiState.value.userEmail
        val credential = EmailAuthProvider.getCredential(email, password)
        Firebase.auth.currentUser!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Firebase.auth.currentUser!!.verifyBeforeUpdateEmail(newEmail)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Firebase.auth.signOut()
                                goToAuthActivity(context)
                            } else updateStatus(Status.Done)
                            notifyUser(task2, context)
                        }
                } else {
                    notifyUser(task, context)
                    updateStatus(Status.Done)
                }
            }
    }

    fun updateUserPassword(context: Context) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        updateStatus(Status.Loading)

        val password = _uiState.value.userPassword
        val newPassword = _uiState.value.newPassword
        val email = Firebase.auth.currentUser!!.email!!
        val credential = EmailAuthProvider.getCredential(email, password)
        Firebase.auth.currentUser!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Firebase.auth.currentUser!!.updatePassword(newPassword)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Firebase.auth.signOut()
                                goToAuthActivity(context)
                            } else updateStatus(Status.Done)
                            notifyUser(task2, context)
                        }
                } else {
                    notifyUser(task, context)
                    updateStatus(Status.Done)
                }
            }
    }

    fun updateUiState(uiState: ProfileUiState) = _uiState.update { uiState }

    private fun updateStatus(status: Status) = _uiState.update { it.copy(status = status) }

    fun checkName(): Boolean {
        val name = _uiState.value.userName
        return name.isNotBlank()
                && name.length >= Constants.NAME_LENGTH
                && name != _uiState.value.user.displayName
    }

    fun checkEmail(): Boolean {
        val email = _uiState.value.userEmail
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && email != _uiState.value.user.email
    }

    fun checkPassword(): Boolean {
        val password = _uiState.value.userPassword
        return password.isNotEmpty()
                && password.length >= Constants.PASSWORD_LENGTH
    }

    fun checkNewPassword(): Boolean {
        val password = _uiState.value.newPassword
        return password.isNotEmpty()
                && password.length >= Constants.PASSWORD_LENGTH
                && password != _uiState.value.userPassword
    }

    private fun goToAuthActivity(context: Context) {
        val activity = (context as MainActivity)
        val intent = Intent(context, AuthActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}

data class ProfileUiState(
    val user: FirebaseUser = Firebase.auth.currentUser!!,
    val userName: String = "",
    val userEmail: String = "",
    val userPassword: String = "",
    val newPassword: String = "",
    val status: Status = Status.Done,
    val updateResult: Boolean = false
)