package com.example.daytask.util.firebase

import com.example.daytask.data.Message
import com.example.daytask.data.Notification
import com.example.daytask.data.NotificationKey
import com.example.daytask.data.SubTask
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.daytask.data.Task as newTask

object FirebaseManager {
    private val database = Firebase.database.reference
    private val userId = Firebase.auth.uid!!
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
        task.memberList.forEach {
            createNotification(it.userId, NotificationKey.Task, task.title)
        }
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

    fun isUserGoogleAuth(): Boolean =
        Firebase.auth.currentUser!!.providerData.map { it.providerId }.contains("google.com")

    fun updateUserStatus(isOnline: Boolean): Task<Void> {
        return userRef.child("isOnline")
            .setValue(isOnline)
    }

    fun uploadPrivateMessage(message: Message): Pair<Task<Void>, Task<Void>> {
        val receiverId = message.receiverId
        val task1 = database.child("users/$receiverId/message/private/$userId").push()
            .setValue(message)
        val task2 = userRef.child("message/private/$receiverId").push()
            .setValue(message)
        createNotification(receiverId, NotificationKey.Message)
        return Pair(task1, task2)
    }

    private fun createNotification(
        receiverId: String,
        actionKey: NotificationKey,
        destinationText: String = ""
    ) {
        val messageText: String
        val action: Pair<String, String>
        when (actionKey) {
            NotificationKey.Message -> {
                messageText = "sent you a new message"
                action = Pair("message", userId)
            }

            NotificationKey.Task -> {
                messageText = "added you to project"
                action = Pair("task", "taskId")
            }
        }

        val notification = Notification(
            senderId = userId,
            receiverId = receiverId,
            messageText = messageText,
            destinationText = destinationText,
            action = action
        )

        database.child("users/$receiverId/notification").push()
            .setValue(notification)
    }

    fun deleteNotification(id: String): Task<Void> {
        return userRef.child("notification")
            .updateChildren(mapOf(id to null))
    }
}