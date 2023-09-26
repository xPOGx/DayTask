package com.example.daytask.ui.screens.authscreens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.daytask.ui.screens.splashscreen.LogoColumn
import com.example.daytask.ui.theme.SplashLogoBigText

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    goToMainActivity: () -> Unit
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    var isLogIn by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.big))
            .verticalScroll(scrollState)
            .animateContentSize()
    ) {
        LogoColumn(
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.extra_big))
                .align(Alignment.CenterHorizontally),
            multi = 1.5f,
            style = SplashLogoBigText
        )
        HeadlineText(
            text = if (isLogIn) stringResource(R.string.welcome_back)
            else stringResource(R.string.create_account),
            modifier = Modifier.padding(top = dimensionResource(R.dimen.extra_big))
        )
        AuthBody(
            uiState = uiState,
            updateUiState = viewModel::updateUiState,
            isLogIn = isLogIn,
            changeIsLogIn = { isLogIn = !isLogIn},
            logIn = {
                // TODO: LogIn process
                goToMainActivity()
            },
            signUp = {
                // TODO: SignUp process
                goToMainActivity()
            },
            googleLogIn = {
                // TODO: Google Auth LogIn
                goToMainActivity()
            },
            googleSignUp = {
                // TODO: Google Auth SignUp
                goToMainActivity()
            },
            enableLogIn = viewModel.checkLogIn(),
            enableSignUp = viewModel.checkSingUp(),
            checkPrivacy = uiState.checkedPrivacy
        )
    }
}
