package com.example.daytask.data

data class Message(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val isRead: Boolean
)