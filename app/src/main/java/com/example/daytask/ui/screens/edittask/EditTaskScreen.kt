package com.example.daytask.ui.screens.edittask

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.AppViewModelProvider

object EditTaskNavigation : NavigationDestination {
    override val route = "edit"
    override val titleRes = R.string.edit_task
    const val taskId = "taskId"
    val routeWithArgs = "$route/{$taskId}"
}

@Composable
fun EditTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

}