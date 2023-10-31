package com.example.daytask.ui.screens.edittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.daytask.data.SubTask
import com.example.daytask.data.Task
import com.example.daytask.data.User
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
import java.util.Calendar
import com.google.android.gms.tasks.Task as GmsTask

class EditTaskViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: String = checkNotNull(savedStateHandle[EditTaskNavigation.taskId])
    private val _uiState = MutableStateFlow(EditTaskUiState())
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
                        newTitle = task.title,
                        newDetail = task.detail,
                        newDate = task.date,
                        newMembersList = task.memberList,
                        newSubTasksList = task.subTasksList,
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
        val state = _uiState.value
        val newTask = Task(
            title = state.newTitle,
            detail = state.newDetail,
            date = state.newDate,
            memberList = state.newMembersList,
            subTasksList = state.newSubTasksList
        )

        return FirebaseManager.updateTask(userId, taskId, newTask)
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