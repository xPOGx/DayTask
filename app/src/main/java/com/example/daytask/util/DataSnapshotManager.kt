package com.example.daytask.util

import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue

object DataSnapshotManager {
    fun DataSnapshot.toTask(): Task {
        val id = this.key
        val date = this.child("date").getValue<Long>()
        val detail = this.child("detail").getValue<String>()
        val title = this.child("title").getValue<String>()
        val taskComplete = this.child("taskComplete").getValue<Boolean>()
        val memberList = this.child("memberList").children.map {
            val displayName = it.child("displayName").getValue<String>()
            val photoUrl = it.child("photoUrl").getValue<String>()
            val userId = it.child("userId").getValue<String>()
            User(
                userId ?: "",
                displayName,
                photoUrl
            )
        }.toList()
        val subTasksList = this.child("subTasksList").children.map {
            val subTaskTitle = it.child("title").getValue<String>()
            val completed = it.child("completed").getValue<Boolean>()
            SubTask(
                subTaskTitle ?: "",
                completed ?: false
            )
        }.toList()
        return Task(
            id ?: "",
            title ?: "",
            detail ?: "",
            memberList,
            date ?: 0L,
            subTasksList,
            taskComplete ?: false
        )
    }

    fun DataSnapshot.toTaskList(): List<Task> = this.children.map { it.toTask() }.toList()
}