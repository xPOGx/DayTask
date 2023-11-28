package com.example.daytask.ui.screens.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.Message
import com.example.daytask.util.firebase.DataSnapshotManager.toMessageList
import com.example.daytask.util.firebase.FirebaseManager
import com.example.daytask.util.firebase.UsersManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: String = checkNotNull(savedStateHandle[ChatDestination.userId])
    private val uid = Firebase.auth.uid!!
    private val database = Firebase.database.reference
    private val ref = database.child("users/$uid/message/private/$userId")

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserInfo()
        listenChat()
    }

    private fun listenChat() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = snapshot.toMessageList()
                _uiState.update { it.copy(messageList = messagesList) }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase database", error.message)
            }
        })
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            UsersManager.data.collectLatest { data ->
                val temp = data.users.find { it.userId == userId }
                temp?.let { user ->
                    _uiState.update { uiState ->
                        uiState.copy(
                            userName = user.displayName,
                            userPhoto = user.photoUrl,
                            isOnline = user.isOnline
                        )
                    }
                }
            }
        }
    }

    fun updateUiState(uiState: ChatUiState) = _uiState.update { uiState }

    fun sendMessage() {
        if (_uiState.value.newMessageText.isBlank()) return
        val message = Message(
            senderId = Firebase.auth.uid!!,
            receiverId = userId,
            message = _uiState.value.newMessageText,
            isRead = false
        )
        FirebaseManager.uploadPrivateMessage(message)
        _uiState.update { it.copy(newMessageText = "") }
    }
}

data class ChatUiState(
    val userName: String? = "",
    val userPhoto: String? = "",
    val isOnline: Boolean = false,
    val newMessageText: String = "",
    val messageList: List<Message> = emptyList()
)