package com.example.daytask.util.firebase

import android.util.Log
import com.example.daytask.data.Task
import com.example.daytask.util.Status
import com.example.daytask.util.firebase.DataSnapshotManager.toTaskList
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

        CoroutineScope(Dispatchers.Default).launch {
            UsersManager.data.collectLatest { data ->
                while (_data.value.status == Status.Loading) delay(100)
                fetchMembersInfo(data)
            }
        }
    }

    private fun fetchMembersInfo(usersData: UsersManagerData) {
        val taskList = _data.value.taskList
        if (taskList.isEmpty()) return
        val usersList = usersData.users
        if (usersList.isEmpty()) return
        val list = taskList.map { task ->
            task.copy(
                memberList = task.memberList.map { user ->
                    val temp = usersList.find { it.userId == user.userId }
                    if (temp != user) temp
                    else user
                }.requireNoNulls()
            )
        }
        if (taskList != list) ref.setValue(list)
    }
}

data class TaskManagerData(
    val taskList: List<Task> = emptyList(),
    val status: Status = Status.Loading
)