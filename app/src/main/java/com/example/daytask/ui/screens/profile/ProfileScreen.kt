package com.example.daytask.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.data.Task
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.ui.theme.TaskTitleText
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White
import com.example.daytask.util.Status

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile
}

enum class ChangeKey {
    NAME,
    EMAIL,
    PASSWORD,
    TASKS,
    PRIVACY,
    SETTINGS
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    signOut: () -> Unit,
    navigateUp: () -> Unit,
    navigateToNewTask: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val tasksList by viewModel.tasksList.collectAsState()
    val context = LocalContext.current

    var changeKey: ChangeKey? by remember { mutableStateOf(null) }

    if (uiState.status == Status.Loading) LoadingDialog()
    if (uiState.updateResult) navigateUp()

    ProfileBody(
        signOut = signOut,
        saveImage = { viewModel.updateUserAvatar(context, it) },
        changeButton = { changeKey = it },
        disabled = viewModel.disabled,
        modifier = modifier.verticalScroll(rememberScrollState())
    )


    when (changeKey) {
        ChangeKey.NAME, ChangeKey.PASSWORD, ChangeKey.EMAIL -> {
            ProfileDialog(
                onDismissRequest = {
                    when (changeKey) {
                        ChangeKey.NAME -> {
                            viewModel.updateUiState(uiState.copy(userName = ""))
                        }

                        ChangeKey.EMAIL -> {
                            viewModel.updateUiState(
                                uiState.copy(
                                    userEmail = "",
                                    userPassword = ""
                                )
                            )
                        }

                        else -> {
                            viewModel.updateUiState(
                                uiState.copy(
                                    userPassword = "",
                                    newPassword = ""
                                )
                            )
                        }
                    }
                    changeKey = null
                },
                saveChanges = {
                    when (changeKey) {
                        ChangeKey.NAME -> viewModel.updateUserName(context)
                        ChangeKey.EMAIL -> viewModel.updateUserEmail(context)
                        else -> viewModel.updateUserPassword(context)
                    }
                },
                enableSave = when (changeKey) {
                    ChangeKey.NAME -> viewModel.checkName()
                    ChangeKey.EMAIL -> viewModel.checkEmail()
                    else -> viewModel.checkPassword() && viewModel.checkNewPassword()
                }
            ) {
                when (changeKey) {
                    ChangeKey.NAME ->
                        ChangeUserNameField(
                            uiState = uiState,
                            updateUiState = viewModel::updateUiState,
                            nameValidation = viewModel::checkName
                        )

                    ChangeKey.EMAIL ->
                        ChangeUserEmailField(
                            uiState = uiState,
                            updateUiState = viewModel::updateUiState,
                            emailValidation = viewModel::checkEmail,
                            passwordValidation = viewModel::checkPassword
                        )

                    else ->
                        ChangeUserPasswordField(
                            uiState = uiState,
                            updateUiState = viewModel::updateUiState,
                            passwordValidation = viewModel::checkPassword,
                            newPasswordValidation = viewModel::checkNewPassword
                        )
                }
            }
        }

        ChangeKey.TASKS -> {
            if (tasksList.isEmpty()) {
                AlertNoTasks(
                    onDismissRequest = { changeKey = null },
                    navigateToNewTask = navigateToNewTask
                )
            } else {
                DeleteTaskDialog(
                    onDismissRequest = { changeKey = null },
                    tasksList = tasksList,
                    deleteTask = viewModel::deleteTask
                )
            }
        }

        else -> {}
    }
}

@Composable
fun AlertNoTasks(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    navigateToNewTask: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.oh_god_no_tasks))
        },
        text = {
            Text(text = stringResource(R.string.fix_error))
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    navigateToNewTask()
                }
            ) {
                Text(text = stringResource(R.string.god_create))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.nah_nothing))
            }
        },
        modifier = modifier
    )
}

@Composable
fun DeleteTaskDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    deleteTask: (String) -> Unit,
    tasksList: List<Task>
) {
    Dialog(onDismissRequest) {
        Column(modifier) {
            Text(
                text = stringResource(R.string.be_careful_task_removal),
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Tertiary)
                    .fillMaxWidth()
            )
            AllTasksListDelete(
                tasksList = tasksList,
                deleteTask = {
                    if (tasksList.size == 1) onDismissRequest()
                    deleteTask(it)
                }
            )
        }
    }
}

@Composable
fun AllTasksListDelete(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    deleteTask: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = dimensionResource(R.dimen.medium))
            .height((LocalConfiguration.current.screenHeightDp / 2).dp)
    ) {
        items(
            items = tasksList,
            key = { it.id }
        ) { task ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Tertiary
                ),
                shape = RectangleShape,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(dimensionResource(R.dimen.medium))
                ) {
                    Text(
                        text = task.title,
                        style = TaskTitleText,
                        color = White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { deleteTask(task.id) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            }
        }
    }
}