package com.example.daytask.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daytask.data.Task
import com.example.daytask.util.CalendarManager
import com.example.daytask.util.Status
import com.example.daytask.util.firebase.TasksManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()
    private var allTaskList: List<Task> = emptyList()

    init {
        viewModelScope.launch {
            TasksManager.data.collectLatest { data ->
                allTaskList = data.taskList
                _uiState.update {
                    it.copy(
                        tasksList = filterTasksList(data.taskList),
                        status = data.status
                    )
                }
            }
        }
    }

    fun updateUiState(uiState: CalendarUiState) = _uiState.update { uiState }

    fun updateDay(day: Int) {
        _uiState.update { it.copy(initDay = day) }
        _uiState.update { it.copy(tasksList = filterTasksList(allTaskList)) }
    }

    private fun filterTasksList(list: List<Task>): List<Task> =
        list.filter { task ->
            task.date in CalendarManager.getDateRangeForDay(_uiState.value.initDay)
        }
}

data class CalendarUiState(
    val initDay: Int = CalendarManager.currentDayOfMonth,
    val status: Status = Status.Loading,
    val tasksList: List<Task> = emptyList()
)