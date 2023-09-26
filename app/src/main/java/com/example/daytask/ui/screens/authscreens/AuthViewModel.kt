package com.example.daytask.ui.screens.authscreens

import androidx.lifecycle.ViewModel
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

    private fun checkEmail(): Boolean {
        val email = _uiState.value.email
        return email.isNotEmpty() && email.contains("@")
    }

    private fun checkPassword(): Boolean {
        val password = _uiState.value.password
        return password.isNotEmpty()
    }

    private fun checkName(): Boolean {
        val name = _uiState.value.fullName
        return name.isNotBlank()
    }
}

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val checkedPrivacy: Boolean = false
)