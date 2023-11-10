package com.example.daytask.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.example.daytask.util.DataSnapshotManager.toTaskList
import com.example.daytask.util.Status
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users/$userId/tasks")

    init {
        initDB()
    }

    private fun initDB() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.update { it.copy(status = Status.Loading) }
                val tasksList = snapshot.toTaskList().sortedBy { it.date }
                viewModelScope.launch(Dispatchers.IO) {
                    fetchMembersInfo(tasksList)
                }
                _uiState.update { state ->
                    state.copy(
                        tasksList = tasksList,
                        ongoingTasks = tasksList.filter { !it.taskComplete },
                        completeTasks = tasksList.filter { it.taskComplete },
                        status = Status.Done
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.update { it.copy(status = Status.Done) }
                Log.e("DB Load Error", error.message)
            }
        })
    }

    private suspend fun fetchMembersInfo(tasksList: List<Task>) {
        val cache = mutableListOf<User>()
        val list = tasksList.map { task ->
            task.copy(
                memberList = task.memberList.map { user ->
                    val temp = cache.filter {
                        it.userId == user.userId
                    }
                    if (temp.isEmpty()) {
                        val ref = database.child("users/${user.userId}")
                        val task1 = ref.child("displayName").get()
                        val task2 = ref.child("photoUrl").get()
                        task1.await()
                        task2.await()
                        val name = task1.result.getValue(String::class.java)
                        val photoUrl = task2.result.getValue(String::class.java)
                        user.copy(
                            displayName = name,
                            photoUrl = photoUrl
                        ).also { cache.add(it) }
                    } else temp.first()
                }
            )
        }
        if (tasksList != list) {
            ref.setValue(list)
        }
    }

    fun updateUiState(uiState: HomeUiState) = _uiState.update { uiState }

    fun updateQuery(query: String) {
        updateUiState(
            _uiState.value.copy(
                query = query,
                loadQueryResult = query.isNotBlank(),
                filteredTasksList = if (query.isNotBlank()) filteredTasksList(query) else emptyList()
            )
        )
    }

    private fun filteredTasksList(queryText: String): List<Task> {
        val query = queryText.lowercase()
        val list = _uiState.value.tasksList
        return list.filter { task ->
            filterTitle(task, query) ||
            filterDetail(task, query) ||
            filterMember(task, query) ||
            filterSubTasks(task, query)
        }
    }

    private fun filterTitle(task: Task, query: String) =
        if (_uiState.value.titleCheck) task.title.lowercase().contains(query) else false

    private fun filterDetail(task: Task, query: String) =
        if (_uiState.value.detailCheck) task.detail.lowercase().contains(query) else false

    private fun filterMember(task: Task, query: String) =
        if (_uiState.value.memberCheck)
            task.memberList.any {
                it.displayName != null && it.displayName.lowercase().contains(query)
            }
        else false

    private fun filterSubTasks(task: Task, query: String) =
        if (_uiState.value.subTaskCheck)
            task.subTasksList.any {
                it.title.lowercase().contains(query)
            }
        else false
}

data class HomeUiState(
    val user: FirebaseUser = Firebase.auth.currentUser!!,
    val tasksList: List<Task> = listOf(),
    val ongoingTasks: List<Task> = listOf(),
    val completeTasks: List<Task> = listOf(),
    val filteredTasksList: List<Task> = listOf(),
    val status: Status = Status.Loading,
    val query: String = "",
    val loadQueryResult: Boolean = false,
    val titleCheck: Boolean = true,
    val detailCheck: Boolean = true,
    val memberCheck: Boolean = false,
    val subTaskCheck: Boolean = false
)
