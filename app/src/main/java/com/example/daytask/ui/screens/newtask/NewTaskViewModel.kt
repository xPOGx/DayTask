package com.example.daytask.ui.screens.newtask

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.daytask.R
import com.example.daytask.data.Task
import com.example.daytask.data.User
import com.example.daytask.ui.screens.tools.FirebaseManager
import com.example.daytask.util.Constants.timeLimitMillis
import com.example.daytask.util.NetworkManager.isNetworkAvailable
import com.example.daytask.util.NotifyManager.notifyUser
import com.example.daytask.util.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class NewTaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NewTaskUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(uiState: NewTaskUiState) = _uiState.update { uiState }

    private fun updateStatus(status: Status) = _uiState.update { it.copy(status = status) }

    fun validNewTask(): Boolean {
        val state = _uiState.value
        val currentTime = Calendar.getInstance().timeInMillis
        return state.title.isNotBlank() &&
                state.details.isNotBlank() &&
                state.memberList.isNotEmpty() &&
                state.date > currentTime
    }

    fun uploadNewTask(context: Context) {
        if (!isNetworkAvailable(context)) {
            notifyUser(context)
            return
        }
        if (!validNewTask()) {
            notifyUser(context, context.getString(R.string.not_valid_task))
            return
        }
        updateStatus(Status.Loading)

        val state = _uiState.value
        val newTask = Task(
            title = state.title,
            detail = state.details,
            memberList = state.memberList,
            date = state.date,
            subTasksList = listOf(),
            taskComplete = false
        )
        FirebaseManager.uploadTask(Firebase.auth.currentUser!!.uid, newTask)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUiState(_uiState.value.copy(updateResult = true))
                } else updateStatus(Status.Done)
                notifyUser(task, context)
            }
    }
}

data class NewTaskUiState(
    val title: String = "",
    val details: String = "",
    val date: Long = Calendar.getInstance().timeInMillis + timeLimitMillis,
    val memberList: List<User> = listOf(),
    val status: Status = Status.Done,
    val updateResult: Boolean = false
)
