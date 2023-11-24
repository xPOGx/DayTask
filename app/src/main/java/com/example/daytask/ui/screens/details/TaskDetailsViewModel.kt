package com.example.daytask.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.util.Constants
import com.example.daytask.util.FirebaseManager
import com.example.daytask.util.Status
import com.example.daytask.util.TasksManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.android.gms.tasks.Task as GmsTask

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: String = checkNotNull(savedStateHandle[TaskDetailsNavigation.taskId])
    private val _uiState = MutableStateFlow(TaskDetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            TasksManager.data.collectLatest { data ->
                val task = data.taskList.find { it.id == taskId }
                _uiState.update { state ->
                    if (task == null) state.copy(
                        status = Status.Error.also { it.message = Constants.NO_TASK }
                    )
                    else state.copy(
                        task = task,
                        status = Status.Done
                    )
                }
            }
        }
    }

    fun addSubTask() {
        val subTask = SubTask(
            title = _uiState.value.title.trim(),
            completed = false
        )
        FirebaseManager.uploadSubTask(taskId, subTask)
    }

    fun updateSubtask(subTaskId: String, completed: Boolean) {
        FirebaseManager.updateSubTask(taskId, subTaskId, completed)
    }

    fun validTitle(): Boolean = _uiState.value.title.isNotBlank()

    fun updateUiState(uiState: TaskDetailsUiState) = _uiState.update { uiState }

    fun finishTask(): GmsTask<Void> {
        val task = _uiState.value.task
        return FirebaseManager.updateTask(
            taskId,
            task.copy(taskComplete = !task.taskComplete)
        )
    }
}

data class TaskDetailsUiState(
    val task: Task = Task(),
    val status: Status = Status.Loading,
    val addStatus: Status = Status.Done,
    val title: String = ""
)