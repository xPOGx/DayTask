package com.example.daytask.ui.screens.tools

import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.daytask.data.Task as newTask

object FirebaseManager {
    private val database = Firebase.database.reference
    fun updateUserName(uid: String, name: String?) {
        database.child("users/$uid/displayName")
            .setValue(name)
    }

    fun updateUserPhoto(uid: String, photoUrl: String) {
        database.child("users/$uid/photoUrl")
            .setValue(photoUrl)
    }

    fun uploadTask(uid: String, task: newTask): Task<Void> {
        return database.child("users/$uid/tasks").push()
            .setValue(task)
    }
}

