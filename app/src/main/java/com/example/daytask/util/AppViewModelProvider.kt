package com.example.daytask.util

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.daytask.ui.screens.chat.ChatViewModel
import com.example.daytask.ui.screens.details.TaskDetailsViewModel
import com.example.daytask.ui.screens.edittask.EditTaskViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TaskDetailsViewModel(
                this.createSavedStateHandle()
            )
        }
        initializer {
            EditTaskViewModel(
                this.createSavedStateHandle()
            )
        }
        initializer {
            ChatViewModel(
                this.createSavedStateHandle()
            )
        }
    }
}