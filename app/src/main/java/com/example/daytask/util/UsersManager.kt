package com.example.daytask.util

import android.util.Log
import com.example.daytask.data.User
import com.example.daytask.util.DataSnapshotManager.toUserList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object UsersManager {
    private var _data: MutableStateFlow<UsersData> = MutableStateFlow(UsersData())
    val data = _data.asStateFlow()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users")

    init {
        getUsers()
    }

    private fun getUsers() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _data.update { it.copy(status = Status.Loading) }
                val usersList = snapshot.toUserList()
                    .filter { it.userId != userId }
                    .sortedBy { it.displayName }
                _data.update {
                    it.copy(
                        users = usersList,
                        status = Status.Done
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase database", error.message)
                _data.update { it.copy(status = Status.Error) }
            }
        })
    }
}

data class UsersData(
    val users: List<User> = emptyList(),
    val status: Status = Status.Loading
)