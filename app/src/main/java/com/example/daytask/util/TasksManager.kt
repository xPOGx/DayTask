package com.example.daytask.util

import android.util.Log
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.example.daytask.util.DataSnapshotManager.toTaskList
import com.example.daytask.util.DataSnapshotManager.toUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object TasksManager {
    private var _data = MutableStateFlow(TaskManagerData())
    val data = _data.asStateFlow()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users/$userId/tasks")

    init {
        initTasks()
    }

    private fun initTasks() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _data.update { it.copy(status = Status.Loading) }
                val taskList = snapshot.toTaskList().sortedBy { it.date }
                CoroutineScope(Dispatchers.Default).launch {
                    fetchMembersInfo(taskList)
                }
                _data.update {
                    it.copy(
                        taskList = taskList,
                        status = Status.Done
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase database", error.message)
                _data.update { it.copy(status = Status.Error) }
            }
        })
    }

    private suspend fun fetchMembersInfo(tasksList: List<Task>) {
        if (tasksList.isEmpty()) return
        val cache = mutableListOf<User>()
        val list = tasksList.map { task ->
            task.copy(
                memberList = task.memberList.map { user ->
                    val temp = cache.find { it.userId == user.userId }
                    if (temp == null) {
                        val job = database.child("users/${user.userId}").get()
                        job.await()
                        job.result
                            .toUser()
                            .also { cache.add(it) }
                    } else temp
                }
            )
        }
        if (tasksList != list) ref.setValue(list)
    }
}

data class TaskManagerData(
    val taskList: List<Task> = emptyList(),
    val status: Status = Status.Loading
)