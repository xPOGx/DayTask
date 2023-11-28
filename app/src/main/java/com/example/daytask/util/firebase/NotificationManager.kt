package com.example.daytask.util.firebase

import android.util.Log
import com.example.daytask.data.Notification
import com.example.daytask.util.Status
import com.example.daytask.util.firebase.DataSnapshotManager.toNotificationList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object NotificationManager {
    private val database = Firebase.database.reference
    private val uid = Firebase.auth.uid
    private val ref = database.child("users/$uid/notification")

    private val _data = MutableStateFlow(NotificationManagerData())
    val data = _data.asStateFlow()

    init {
        listenNotification()
    }

    private fun listenNotification() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _data.update { it.copy(status = Status.Loading) }
                val notificationList = snapshot.toNotificationList()
                _data.update {
                    it.copy(
                        notifications = notificationList,
                        status = Status.Done
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _data.update { it.copy(status = Status.Error) }
                Log.e("Firebase database", error.message)
            }
        })
    }
}

data class NotificationManagerData(
    val notifications: List<Notification> = emptyList(),
    val status: Status = Status.Loading
)