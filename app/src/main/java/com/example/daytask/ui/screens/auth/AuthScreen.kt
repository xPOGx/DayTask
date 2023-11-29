package com.example.daytask.ui.screens.auth

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.ui.screens.splash.LogoColumn
import com.example.daytask.ui.theme.HeadlineText
import com.example.daytask.ui.theme.SplashLogoBigText
import com.example.daytask.ui.theme.White

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    signUp: (String, String, String) -> Unit,
    logIn: (String, String) -> Unit,
    googleSignIn: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isLogIn by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.big))
            .verticalScroll(rememberScrollState())
            .animateContentSize()
    ) {
        LogoColumn(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.extra_big))
                .align(Alignment.CenterHorizontally),
            multi = 1.5f,
            style = SplashLogoBigText
        )
        Text(
            text = if (isLogIn)
                stringResource(R.string.welcome_back) else stringResource(R.string.create_account),
            color = White,
            style = HeadlineText,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.extra_big))
        )
        AuthBody(
            uiState = uiState,
            updateUiState = viewModel::updateUiState,
            isLogIn = isLogIn,
            changeIsLogIn = { isLogIn = !isLogIn },
            logIn = { logIn(uiState.email, uiState.password)  },
            signUp = { signUp(uiState.email, uiState.password, uiState.fullName) },
            googleSignIn = googleSignIn,
            checkPrivacy = uiState.checkedPrivacy,
            validEmail = viewModel::checkEmail,
            validPassword = viewModel::checkPassword,
            validName = viewModel::checkName,
            enableLogIn = viewModel.checkLogIn(),
            enableSignUp = viewModel.checkSingUp()
        )
    }
}
