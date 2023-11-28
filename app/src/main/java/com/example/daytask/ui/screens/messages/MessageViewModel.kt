package com.example.daytask.ui.screens.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.daytask.data.Chat
import com.example.daytask.util.firebase.DataSnapshotManager.toChatList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private val userId = Firebase.auth.uid!!
    private val ref = database.child("users/$userId/message/private")

    private val _chats = MutableStateFlow(emptyList<Chat>())
    val chat = _chats.asStateFlow()

    init {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatsList = snapshot.toChatList()
                _chats.update { chatsList }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase database", error.message)
            }
        })
    }
}