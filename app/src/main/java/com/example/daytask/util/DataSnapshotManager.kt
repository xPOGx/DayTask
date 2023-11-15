package com.example.daytask.util

import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.google.firebase.database.DataSnapshot

object DataSnapshotManager {
    fun DataSnapshot.toTask(): Task {
        val id = this.key!!
        val date = this.child("date").getValue(Long::class.java)!!
        val detail = this.child("detail").getValue(String::class.java)!!
        val title = this.child("title").getValue(String::class.java)!!
        val taskComplete = this.child("taskComplete").getValue(Boolean::class.java)!!
        val memberList = this.child("memberList").toMemberList()
        val subTasksList = this.child("subTasksList").toSubTaskList()
        return Task(
            id,
            title,
            detail,
            memberList,
            date,
            subTasksList,
            taskComplete
        )
    }

    private fun DataSnapshot.toMember(): User {
        val userId = this.child("userId").getValue(String::class.java)!!
        val displayName = this.child("displayName").getValue(String::class.java)
        val photoUrl = this.child("photoUrl").getValue(String::class.java)
        return User(
            userId,
            displayName,
            photoUrl
        )
    }

    private fun DataSnapshot.toMemberList(): List<User> = this.children.map { it.toMember() }

    private fun DataSnapshot.toSubTask(): SubTask {
        val subTaskId = this.key!!
        val subTaskTitle = this.child("title").getValue(String::class.java)!!
        val completed = this.child("completed").getValue(Boolean::class.java)!!
        return SubTask(
            subTaskId,
            subTaskTitle,
            completed
        )
    }

    fun DataSnapshot.toTaskList(): List<Task> = this.children.map { it.toTask() }.toList()
    private fun DataSnapshot.toSubTaskList(): List<SubTask> = this.children.map { it.toSubTask() }

    fun DataSnapshot.toUser(): User {
        val userId = this.key!!
        val displayName = this.child("displayName").getValue(String::class.java)
        val photoUrl = this.child("photoUrl").getValue(String::class.java)
        return User(
            userId,
            displayName,
            photoUrl
        )
    }

    fun DataSnapshot.toUserList(): List<User> = this.children.map { it.toUser() }
}