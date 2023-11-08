package com.example.daytask.ui.screens.details

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.AppViewModelProvider
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.util.Status

object TaskDetailsNavigation : NavigationDestination {
    override val route = "details"
    override val titleRes = R.string.task_details
    const val taskId = "taskId"
    val routeWithArgs = "$route/{$taskId}"
}

@Composable
fun TaskDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DetailDialog(
            onDismissRequest = {
                viewModel.updateUiState(uiState.copy(title = ""))
                showDialog = false
            },
            title = uiState.title,
            validTitle = viewModel.validTitle(),
            updateTitle = { viewModel.updateUiState(uiState.copy(title = it)) },
            buttonAction = viewModel::addSubTask
        )
    }

    when (uiState.status) {
        Status.Loading -> LoadingDialog()
        Status.Error -> {
            Toast.makeText(LocalContext.current, uiState.status.message, Toast.LENGTH_SHORT).show()
            navigateUp()
        }

        Status.Done -> {
            TaskDetailBody(
                uiState = uiState,
                updateSubTask = viewModel::updateSubtask,
                showDialog = { showDialog = true },
                modifier = modifier
            )
        }
    }
}