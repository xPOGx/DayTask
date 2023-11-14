package com.example.daytask.ui.screens.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.daytask.data.Task
import com.example.daytask.util.CalendarManager
import com.example.daytask.util.DataSnapshotManager.toTaskList
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

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    private val database = Firebase.database.reference
    private val userId = Firebase.auth.currentUser!!.uid
    private val ref = database.child("users/$userId/tasks")

    private var tasksList: List<Task> = emptyList()

    init {
        initDB()
    }

    private fun initDB() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _uiState.update { it.copy(status = Status.Loading) }
                tasksList = snapshot.toTaskList()
                filterTasksList()
                _uiState.update { it.copy(status = Status.Done) }
            }

            override fun onCancelled(error: DatabaseError) {
                _uiState.update { it.copy(status = Status.Error) }
                Log.e("DB Load Error", error.message)
            }
        })
    }

    fun updateUiState(uiState: CalendarUiState) = _uiState.update { uiState }

    fun updateDay(day: Int) {
        _uiState.update { it.copy(initDay = day) }
        filterTasksList()
    }

    private fun filterTasksList() {
        val filteredTasks = tasksList.filter {
            it.date in CalendarManager.getDateRangeForDay(_uiState.value.initDay)
        }
        _uiState.update { it.copy(tasksList = filteredTasks) }
    }
}

data class CalendarUiState(
    val initDay: Int = CalendarManager.currentDayOfMonth,
    val status: Status = Status.Loading,
    val tasksList: List<Task> = emptyList()
)