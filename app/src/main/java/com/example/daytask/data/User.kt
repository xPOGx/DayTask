package com.example.daytask.data


data class User(
    val userId: String,
    val displayName: String?,
    val photoUrl: String?,
    val isOnline: Boolean
)