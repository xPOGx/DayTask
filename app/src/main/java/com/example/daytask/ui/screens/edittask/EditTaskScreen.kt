package com.example.daytask.ui.screens.edittask

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.util.AppViewModelProvider
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.util.Status

object EditTaskNavigation : NavigationDestination {
    override val route = "edit"
    override val titleRes = R.string.edit_task
    const val taskId = "taskId"
    val routeWithArgs = "$route/{$taskId}"
}

@Composable
fun EditTaskScreen(
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (uiState.status) {
        Status.Loading -> LoadingDialog()
        Status.Error -> {
            LaunchedEffect(key1 = "error") {
                Toast.makeText(context, uiState.status.message, Toast.LENGTH_SHORT).show()
                navigateUp()
            }
        }

        Status.Done -> {
            EditTaskBody(
                uiState = uiState,
                updateUiState = viewModel::updateUiState,
                validSave = viewModel.validSave(),
                updateTask = {
                    viewModel.updateTask()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navigateUp()
                            } else {
                                val text = task.exception?.message ?: "Error update"
                                Toast.makeText(
                                    context,
                                    text,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                },
                modifier = modifier
            )
        }
    }
}