package com.example.daytask.data

data class Notification(
    val id: String = "",
    val senderId: String,
    val receiverId: String,
    val messageText: String,
    val destinationText: String,
    val action: Pair<String, String>
)

enum class NotificationKey {
    Message,
    Task
}