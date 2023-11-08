package com.example.daytask.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.util.DataSnapshotManager.toTask
import com.example.daytask.util.FirebaseManager
import com.example.daytask.util.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.google.android.gms.tasks.Task as GmsTask

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: String = checkNotNull(savedStateHandle[TaskDetailsNavigation.taskId])
    private val _uiState = MutableStateFlow(TaskDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users/$userId/tasks/$taskId")

    init {
        initTask()
    }

    private fun initTask() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val task = snapshot.toTask()
                _uiState.update {
                    it.copy(
                        task = task,
                        status = Status.Done
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                updateUiState(
                    _uiState.value.copy(
                        status = Status.Error.also {
                            it.message = error.message
                        }
                    )
                )
            }
        })
    }

    fun addSubTask() {
        val subTask = SubTask(
            title = _uiState.value.title.trim(),
            completed = false
        )
        FirebaseManager.uploadSubTask(userId, taskId, subTask)
    }

    fun updateSubtask(subTaskId: String, completed: Boolean) {
        FirebaseManager.updateSubTask(userId, taskId, subTaskId, completed)
    }

    fun validTitle(): Boolean = _uiState.value.title.isNotBlank()

    fun updateUiState(uiState: TaskDetailsUiState) = _uiState.update { uiState }

    fun finishTask(): GmsTask<Void> {
        val task = _uiState.value.task
        return FirebaseManager.updateTask(
            userId,
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