package com.example.daytask.util

import android.util.Log
import com.example.daytask.data.User
import com.example.daytask.util.DataSnapshotManager.toUserList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object UsersManager {
    private var _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users = _users.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        Firebase.database.reference.child("users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _users.update {
                        task.result
                            .toUserList()
                            .filter { it.userId != Firebase.auth.currentUser!!.uid }
                            .sortedBy { it.displayName }
                    }
                } else {
                    Log.e("Firebase Error", task.exception.toString())
                }
            }
    }
}