package com.example.daytask.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.InputField
import com.example.daytask.util.Constants
import com.example.daytask.util.FirebaseManager

@Composable
fun ChangeUserNameField(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    updateUiState: (ProfileUiState) -> Unit,
    nameValidation: () -> Boolean
) {
    InputField(
        headlineText = stringResource(R.string.new_name),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
        inputText = uiState.userName,
        onValueChange = { updateUiState(uiState.copy(userName = it)) },
        leadingIconRes = R.drawable.ic_useradd_profile,
        errorTextRes = if (uiState.userName.length < Constants.NAME_LENGTH)
            R.string.invalid_name else R.string.different,
        validation = nameValidation,
        modifier = modifier
    )
}

@Composable
fun ChangeUserEmailField(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    updateUiState: (ProfileUiState) -> Unit,
    emailValidation: () -> Boolean,
    passwordValidation: () -> Boolean
) {
    InputField(
        headlineText = stringResource(R.string.new_email),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Email,
        inputText = uiState.userEmail,
        onValueChange = { updateUiState(uiState.copy(userEmail = it)) },
        leadingIconRes = R.drawable.ic_usertag_profile,
        errorTextRes = if (uiState.userEmail == FirebaseManager.userEmail)
            R.string.different else R.string.invalid_email,
        validation = emailValidation,
        modifier = modifier
    )
    InputField(
        headlineText = stringResource(R.string.current_password),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        isHidden = true,
        inputText = uiState.userPassword,
        onValueChange = { updateUiState(uiState.copy(userPassword = it)) },
        leadingIconRes = R.drawable.ic_lock_profile,
        trailingIconsRes = Pair(R.drawable.ic_eyeslash, R.drawable.ic_eye),
        errorTextRes = if (uiState.userPassword.length < Constants.PASSWORD_LENGTH)
            R.string.invalid_password else R.string.different,
        validation = passwordValidation,
        modifier = modifier
    )
}

@Composable
fun ChangeUserPasswordField(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    updateUiState: (ProfileUiState) -> Unit,
    passwordValidation: () -> Boolean,
    newPasswordValidation: () -> Boolean
) {
    InputField(
        headlineText = stringResource(R.string.current_password),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        isHidden = true,
        inputText = uiState.userPassword,
        onValueChange = { updateUiState(uiState.copy(userPassword = it)) },
        leadingIconRes = R.drawable.ic_lock_profile,
        trailingIconsRes = Pair(R.drawable.ic_eyeslash, R.drawable.ic_eye),
        errorTextRes = if (uiState.userPassword.length < Constants.PASSWORD_LENGTH)
            R.string.invalid_password else R.string.different,
        validation = passwordValidation,
        modifier = modifier
    )
    InputField(
        headlineText = stringResource(R.string.new_password),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Password,
        isHidden = true,
        inputText = uiState.newPassword,
        onValueChange = { updateUiState(uiState.copy(newPassword = it)) },
        leadingIconRes = R.drawable.ic_lock_profile,
        trailingIconsRes = Pair(R.drawable.ic_eyeslash, R.drawable.ic_eye),
        errorTextRes = if (uiState.newPassword.length < Constants.PASSWORD_LENGTH)
            R.string.invalid_password else R.string.different,
        validation = newPasswordValidation,
        modifier = modifier
    )
}