package com.example.daytask.ui.screens.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.daytask.activity.AuthActivity
import com.example.daytask.activity.MainActivity
import com.example.daytask.data.Task
import com.example.daytask.util.Constants
import com.example.daytask.util.DataSnapshotManager.toTaskList
import com.example.daytask.util.FirebaseManager
import com.example.daytask.util.NetworkManager.isNetworkAvailable
import com.example.daytask.util.NotifyManager.notifyUser
import com.example.daytask.util.Status
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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
    val disabled = FirebaseManager.isUserGoogleAuth()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users/$userId/tasks")
    private var _tasksList: MutableStateFlow<List<Task>> = MutableStateFlow(emptyList())
    val tasksList = _tasksList.asStateFlow()

    init {
        initDB()
    }

    private fun initDB() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasksList = snapshot.toTaskList().sortedBy { it.date }
                _tasksList.update { tasksList }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DB Load Error", error.message)
            }
        })
    }

    fun updateUserName(context: Context) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        updateStatus(Status.Loading)

        Firebase.auth.currentUser!!.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(_uiState.value.userName.trim())
                .build()
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseManager.updateUserName(
                        Firebase.auth.currentUser!!.displayName
                    )
                    updateUiState(_uiState.value.copy(updateResult = true))
                } else updateStatus(Status.Done)
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
            .child("users/${userId}/images/$uuid")

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
                                        if (task3.isSuccessful) {
                                            val url = Firebase.auth.currentUser!!.photoUrl.toString()
                                            FirebaseManager.updateUserPhoto(url)
                                            updateUiState(_uiState.value.copy(updateResult = true))
                                        } else updateStatus(Status.Done)
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
        val email = Firebase.auth.currentUser!!.email.toString()
        val newEmail = _uiState.value.userEmail.trim()
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
        val email = Firebase.auth.currentUser!!.email.toString()
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
                && name != Firebase.auth.currentUser!!.displayName
    }

    fun checkEmail(): Boolean {
        val email = _uiState.value.userEmail
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && email != Firebase.auth.currentUser!!.email
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

    fun deleteTask(id: String) {
        FirebaseManager.deleteTask(id).addOnCompleteListener {
            if (!it.isSuccessful) Log.e("Firebase Error", it.exception.toString())
        }
    }
}

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userPassword: String = "",
    val newPassword: String = "",
    val status: Status = Status.Done,
    val updateResult: Boolean = false
)