package com.example.daytask.ui.screens.profile

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.daytask.ui.screens.tools.LoadingDialog
import com.example.daytask.util.Status

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile
}

enum class ChangeKey {
    NAME,
    EMAIL,
    PASSWORD,
    TASKS
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
            MyTaskDialogs(
                onDismissRequest = { changeKey = null },
                tasksList = tasksList,
                deleteTask = viewModel::deleteTask,
                navigateToNewTask = navigateToNewTask
            )
        }

        else -> {}
    }
}

