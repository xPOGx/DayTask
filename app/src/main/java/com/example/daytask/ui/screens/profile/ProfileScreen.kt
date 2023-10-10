package com.example.daytask.ui.screens.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.DayTaskTopAppBar
import com.example.daytask.ui.screens.tools.LoadingDialog

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val titleRes = R.string.profile
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    signOut: () -> Unit,
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    var showChangeName by remember { mutableStateOf(false) }
    var showChangeEmail by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }

    if (uiState.status == Status.Loading) LoadingDialog()
    if (uiState.updateResult) navigateUp()

    Scaffold(
        topBar = {
            DayTaskTopAppBar(
                titleRes = ProfileDestination.titleRes,
                navigateUp = navigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        ProfileBody(
            signOut = signOut,
            userName = uiState.user.displayName,
            userEmail = uiState.user.email,
            userPhoto = uiState.user.photoUrl,
            saveImage = { viewModel.updateUserAvatar(context, it) },
            updateStatus = { viewModel.updateStatus(Status.Loading) },
            changeName = { showChangeName = true },
            changeEmail = { showChangeEmail = true },
            changePassword = { showChangePassword = true },
            disabled = viewModel.disabled,
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        )
    }

    if (showChangeName || showChangeEmail || showChangePassword) {
        ProfileDialog(
            onDismissRequest = {
                when {
                    showChangeName -> {
                        showChangeName = false
                        viewModel.updateUiState(uiState.copy(userName = ""))
                    }

                    showChangeEmail -> {
                        showChangeEmail = false
                        viewModel.updateUiState(
                            uiState.copy(
                                userEmail = "",
                                userPassword = ""
                            )
                        )
                    }

                    showChangePassword -> {
                        showChangePassword = false
                        viewModel.updateUiState(
                            uiState.copy(
                                userPassword = "",
                                newPassword = ""
                            )
                        )
                    }
                }
            },
            saveChanges = {
                when {
                    showChangeName -> viewModel.updateUserName(context)
                    showChangeEmail -> viewModel.updateUserEmail(context)
                    showChangePassword -> viewModel.updateUserPassword(context)
                }
            },
            enableSave = when {
                showChangeName -> viewModel.checkName()
                showChangeEmail -> viewModel.checkEmail()
                else -> viewModel.checkPassword() && viewModel.checkNewPassword()
            }
        ) {
            when {
                showChangeName ->
                    ChangeUserNameField(
                        uiState = uiState,
                        updateUiState = viewModel::updateUiState,
                        nameValidation = viewModel::checkName
                    )

                showChangeEmail ->
                    ChangeUserEmailField(
                        uiState = uiState,
                        updateUiState = viewModel::updateUiState,
                        emailValidation = viewModel::checkEmail,
                        passwordValidation = viewModel::checkPassword
                    )

                showChangePassword ->
                    ChangeUserPasswordField(
                        uiState = uiState,
                        updateUiState = viewModel::updateUiState,
                        passwordValidation = viewModel::checkPassword,
                        newPasswordValidation = viewModel::checkNewPassword
                    )
            }
        }
    }
}
