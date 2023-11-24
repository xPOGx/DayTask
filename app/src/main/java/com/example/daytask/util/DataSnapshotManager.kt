package com.example.daytask.util

import com.example.daytask.data.Chat
import com.example.daytask.data.Message
import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.google.firebase.database.DataSnapshot

object DataSnapshotManager {
    private fun DataSnapshot.toTask(): Task {
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
        return makeUser(userId, this)
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

    private fun DataSnapshot.toUser(): User {
        val userId = this.key!!
        return makeUser(userId, this)
    }

    fun DataSnapshot.toUserList(): List<User> = this.children.map { it.toUser() }

    private fun DataSnapshot.toMessage(): Message {
        val senderId = this.child("senderId").getValue(String::class.java)!!
        val receiverId = this.child("receiverId").getValue(String::class.java)!!
        val messageText = this.child("message").getValue(String::class.java)!!
        val isRead = this.child("read").getValue(Boolean::class.java)!!
        return Message(
            senderId,
            receiverId,
            messageText,
            isRead
        )
    }

    fun DataSnapshot.toMessageList(): List<Message> = this.children.map { message ->
        message.toMessage()
    }

    fun DataSnapshot.toChatList(): List<Chat> = this.children.map { chat ->
        val userId = chat.key!!
        val messagesList = chat.toMessageList()
        Chat(
            userId,
            messagesList
        )
    }

    private fun makeUser(userId: String, dataSnapshot: DataSnapshot): User {
        val displayName = dataSnapshot.child("displayName").getValue(String::class.java)
        val photoUrl = dataSnapshot.child("photoUrl").getValue(String::class.java)
        val isOnline = dataSnapshot.child("isOnline").getValue(Boolean::class.java)
        return User(
            userId,
            displayName,
            photoUrl,
            isOnline = isOnline ?: false
        )
    }
}