package com.example.daytask.util

import com.example.daytask.data.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FakeManager {
    private fun fakeUser(): User {
        val firebaseUser = Firebase.auth.currentUser!!
        return User(
            firebaseUser.uid,
            firebaseUser.displayName,
            firebaseUser.photoUrl.toString()
        )
    }

    val fakeUser = fakeUser()
}