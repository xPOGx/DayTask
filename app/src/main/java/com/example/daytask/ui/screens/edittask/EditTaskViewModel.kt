package com.example.daytask.ui.screens.edittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.daytask.data.Task
import com.example.daytask.util.DataSnapshotManager.toTask
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
}

data class EditTaskUiState(
    val task: Task = Task(),
    val status: Status = Status.Loading
)