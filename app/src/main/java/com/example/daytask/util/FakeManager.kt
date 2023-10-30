package com.example.daytask.util

import com.example.daytask.data.SubTask
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
    val fakeSubTaskList = listOf(
        SubTask(
            title = "User Interviews",
            completed =  true
        ),
        SubTask(
            title = "Wireframes",
            completed =true
        ),
        SubTask(
            title = "Design System",
            completed =true
        ),
        SubTask(
            title = "Icons",
            completed =false
        ),
        SubTask(
            title = "Final Mockups",
            completed =false
        )
    )
}