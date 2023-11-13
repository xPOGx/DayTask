package com.example.daytask.ui.screens.newtask

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.util.Status

object NewTaskDestination : NavigationDestination {
    override val route = "new_task"
    override val titleRes = R.string.create_new_task
}

@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: NewTaskViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.status == Status.Loading) LoadingDialog()

    LaunchedEffect(key1 = uiState.updateResult) {
        if (uiState.updateResult) navigateUp()
    }

    NewTaskBody(
        uiState = uiState,
        validCreate = viewModel.validNewTask(),
        updateUiState = viewModel::updateUiState,
        saveTask = { viewModel.uploadNewTask(context) },
        modifier = modifier.imePadding()
    )
}
