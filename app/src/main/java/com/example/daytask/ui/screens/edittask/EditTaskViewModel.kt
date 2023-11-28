package com.example.daytask.ui.screens.edittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.example.daytask.util.Constants
import com.example.daytask.util.firebase.FirebaseManager
import com.example.daytask.util.Status
import com.example.daytask.util.firebase.TasksManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import com.google.android.gms.tasks.Task as GmsTask

class EditTaskViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: String = checkNotNull(savedStateHandle[EditTaskNavigation.taskId])
    private val _uiState = MutableStateFlow(EditTaskUiState())
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
                        newTitle = task.title,
                        newDetail = task.detail,
                        newDate = task.date,
                        newMembersList = task.memberList,
                        newSubTasksList = task.subTasksList,
                        status = Status.Done
                    )
                }
            }
        }
    }

    fun updateUiState(uiState: EditTaskUiState) = _uiState.update { uiState }

    private fun haveChanges(): Boolean {
        val state = _uiState.value
        val task = state.task
        return state.newTitle != task.title ||
                state.newDetail != task.detail ||
                state.newDate != task.date ||
                state.newMembersList != task.memberList ||
                state.newSubTasksList != task.subTasksList
    }

    private fun contentValidation(): Boolean {
        val state = _uiState.value
        val currentTime = Calendar.getInstance().timeInMillis
        return state.newTitle.isNotBlank() &&
                state.newDetail.isNotBlank() &&
                state.newDate > currentTime
    }

    fun validSave(): Boolean = haveChanges() && contentValidation()

    fun updateTask(): GmsTask<Void> {
        val newTask = with(_uiState.value) {
            task.copy(
                title = newTitle,
                detail = newDetail,
                date = newDate,
                memberList = newMembersList,
                subTasksList = newSubTasksList
            )
        }

        return FirebaseManager.updateTask(taskId, newTask)
    }
}

data class EditTaskUiState(
    val task: Task = Task(),
    val status: Status = Status.Loading,
    val newTitle: String = "",
    val newDetail: String = "",
    val newDate: Long = 0L,
    val newMembersList: List<User> = listOf(),
    val newSubTasksList: List<SubTask> = listOf(),
    val newSubTaskTitle: String = ""
)