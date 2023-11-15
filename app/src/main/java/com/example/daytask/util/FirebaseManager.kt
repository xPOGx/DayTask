package com.example.daytask.util

import com.example.daytask.data.SubTask
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.daytask.data.Task as newTask

object FirebaseManager {
    val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val userRef = database.child("users/$userId")

    fun updateUserName(name: String?): Task<Void> {
        return userRef.child("displayName")
            .setValue(name)
    }

    fun updateUserPhoto(photoUrl: String): Task<Void> {
        return userRef.child("photoUrl")
            .setValue(photoUrl)
    }

    fun uploadTask(task: newTask): Task<Void> {
        return userRef.child("tasks").push()
            .setValue(task)
    }

    fun uploadSubTask(taskId: String, subTask: SubTask): Task<Void> {
        return userRef.child("tasks/$taskId/subTasksList").push()
            .setValue(subTask)
    }

    fun updateSubTask(taskId: String, subTaskId: String, completed: Boolean): Task<Void> {
        return userRef.child("tasks/$taskId/subTasksList/$subTaskId")
            .updateChildren(
                mapOf<String, Any>("completed" to completed)
            )
    }

    fun updateTask(taskId: String, task: newTask): Task<Void> {
        return userRef.child("tasks/$taskId")
            .updateChildren(
                mapOf<String, Any>(
                    "title" to task.title,
                    "detail" to task.detail,
                    "date" to task.date,
                    "memberList" to task.memberList,
                    "subTasksList" to task.subTasksList,
                    "taskComplete" to task.taskComplete
                )
            )
    }

    fun deleteTask(taskId: String): Task<Void> {
        return userRef.child("tasks")
            .updateChildren(mapOf(taskId to null))
    }
}

