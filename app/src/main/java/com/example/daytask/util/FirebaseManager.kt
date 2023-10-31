package com.example.daytask.util

import com.example.daytask.data.SubTask
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

    fun uploadSubTask(uid: String, taskId: String, subTask: SubTask): Task<Void> {
        return database.child("users/$uid/tasks/$taskId/subTasksList").push()
            .setValue(subTask)
    }

    fun updateSubTask(uid: String, taskId: String, subTaskId: String, completed: Boolean): Task<Void> {
        return database.child("users/$uid/tasks/$taskId/subTasksList/$subTaskId")
            .updateChildren(
                mapOf<String, Any>("completed" to completed)
            )
    }

    fun updateTask(uid: String, taskId: String, task: newTask): Task<Void> {
        return database.child("users/$uid/tasks/$taskId")
            .updateChildren(
                mapOf<String, Any>(
                    "title" to task.title,
                    "detail" to task.detail,
                    "date" to task.date,
                    "memberList" to task.memberList,
                    "subTasksList" to task.subTasksList
                )
            )
    }
}

