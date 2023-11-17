package com.example.daytask.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.Task
import com.example.daytask.util.Status
import com.example.daytask.util.TasksManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            TasksManager.data.collectLatest { data ->
                _uiState.update { state ->
                    state.copy(
                        taskList = data.taskList,
                        ongoingTasks = data.taskList.filter { !it.taskComplete },
                        completeTasks = data.taskList.filter { it.taskComplete },
                        status = data.status
                    )
                }
            }
        }
    }

    fun updateUiState(uiState: HomeUiState) = _uiState.update { uiState }

    fun updateQuery(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                loadQueryResult = query.isNotBlank(),
                filteredTasksList = if (query.isNotBlank()) filteredTasksList(query) else emptyList()
            )
        }
    }

    private fun filteredTasksList(queryText: String): List<Task> {
        val query = queryText.lowercase()
        return _uiState.value.taskList.filter { task ->
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
    val taskList: List<Task> = emptyList(),
    val ongoingTasks: List<Task> = emptyList(),
    val completeTasks: List<Task> = emptyList(),
    val filteredTasksList: List<Task> = emptyList(),
    val status: Status = Status.Loading,
    val query: String = "",
    val loadQueryResult: Boolean = false,
    val titleCheck: Boolean = true,
    val detailCheck: Boolean = true,
    val memberCheck: Boolean = false,
    val subTaskCheck: Boolean = false
)
