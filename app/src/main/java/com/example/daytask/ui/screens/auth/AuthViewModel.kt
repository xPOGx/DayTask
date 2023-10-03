package com.example.daytask.ui.screens.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.daytask.ui.screens.tools.Constants.NAME_LENGTH
import com.example.daytask.ui.screens.tools.Constants.PASSWORD_LENGTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun updateUiState(uiState: AuthUiState) {
        _uiState.update {
            uiState
        }
    }

    fun checkLogIn(): Boolean = checkEmail() && checkPassword()

    fun checkSingUp(): Boolean = checkName() && checkLogIn() && _uiState.value.checkedPrivacy

    fun checkEmail(): Boolean {
        val email = _uiState.value.email
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkPassword(): Boolean {
        val password = _uiState.value.password
        return password.isNotEmpty() && password.length >= PASSWORD_LENGTH
    }

    fun checkName(): Boolean {
        val name = _uiState.value.fullName
        return name.isNotBlank() && name.length >= NAME_LENGTH
    }
}

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val checkedPrivacy: Boolean = false
)