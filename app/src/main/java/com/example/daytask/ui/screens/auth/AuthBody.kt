package com.example.daytask.ui.screens.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.MainButton
import com.example.daytask.ui.screens.tools.MultiColorText
import com.example.daytask.ui.theme.GoogleText
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.White

@Composable
fun AuthBody(
    modifier: Modifier = Modifier,
    isLogIn: Boolean,
    changeIsLogIn: () -> Unit,
    uiState: AuthUiState,
    updateUiState: (AuthUiState) -> Unit,
    logIn: () -> Unit,
    signUp: () -> Unit,
    googleLogIn: () -> Unit,
    googleSignUp: () -> Unit,
    validEmail: () -> Boolean,
    validPassword: () -> Boolean,
    validName: () -> Boolean,
    enableLogIn: Boolean,
    enableSignUp: Boolean,
    checkPrivacy: Boolean
) {
    Column(
        modifier = modifier
    ) {
        if (!isLogIn) {
            InputColumn(
                headlineText = stringResource(R.string.full_name),
                inputText = uiState.fullName,
                onValueChange = {
                    updateUiState(uiState.copy(fullName = it))
                },
                leadingIconRes = R.drawable.user,
                validation = validName,
                errorTextRes = R.string.invalid_name,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.small))
            )
        }
        EmailPasswordInput(
            uiState = uiState,
            updateUiState = updateUiState,
            validEmail = validEmail,
            validPassword = validPassword,
            errorEmailRes = R.string.invalid_email,
            errorPasswordRes = R.string.invalid_password,
            modifier = Modifier.padding(
                top = if (isLogIn)
                    dimensionResource(R.dimen.small) else dimensionResource(R.dimen.medium)
            )
        )
        if (isLogIn) {
            ForgotText(
                text = stringResource(R.string.forgot_password),
                onClick = {
                    // TODO: forgot password Send
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = dimensionResource(R.dimen.small))
            )
        } else {
            TermsRow(
                readPrivacy = {
                    // TODO: read Privacy
                },
                readTerms = {
                    // TODO: read Terms
                },
                checked = checkPrivacy,
                changeChecked = {
                    updateUiState(uiState.copy(checkedPrivacy = !checkPrivacy))
                },
                modifier = Modifier.padding(top = dimensionResource(R.dimen.small))
            )
        }
        ButtonColumn(
            mainButtonTextRes = if (isLogIn) R.string.log_in else R.string.sign_up,
            enabledMainButton = if (isLogIn) enableLogIn else enableSignUp,
            onMainClick = if (isLogIn) logIn else signUp,
            onGoogleClick = if (isLogIn) googleLogIn else googleSignUp,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.big))
        )
        MultiColorText(
            if (isLogIn) arrayOf(
                Pair(stringResource(R.string.no_account), HelpColor),
                Pair(stringResource(R.string.sign_up), MainColor)
            ) else arrayOf(
                Pair(stringResource(R.string.already_have_account), HelpColor),
                Pair(stringResource(R.string.log_in), MainColor)
            ),
            style = HelpText,
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.big))
                .clickable(
                    onClick = changeIsLogIn
                )
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ForgotText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Text(
        text = text,
        style = HelpText,
        color = HelpColor,
        modifier = modifier.clickable(onClick = onClick)
    )
}

@Composable
fun TermsRow(
    checked: Boolean,
    changeChecked: () -> Unit,
    readPrivacy: () -> Unit,
    readTerms: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = changeChecked
        ) {
            Icon(
                painter = painterResource(if (checked) R.drawable.ticksquare else R.drawable.square),
                contentDescription = null,
                tint = if (checked) MainColor else White
            )
        }
        AnnotatedClickableText(
            readPrivacy = readPrivacy,
            readTerms = readTerms
        )
    }
}

@Composable
fun ButtonColumn(
    modifier: Modifier = Modifier,
    @StringRes mainButtonTextRes: Int,
    enabledMainButton: Boolean,
    onMainClick: () -> Unit,
    onGoogleClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        MainButton(
            onClick = onMainClick,
            enabled = enabledMainButton,
            text = stringResource(mainButtonTextRes),
            modifier = Modifier
                .height(dimensionResource(R.dimen.large))
                .fillMaxWidth()
        )
        DividerRow(
            textRes = R.string.or_continue,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.extra_big))
        )
        GoogleButton(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.extra_big)),
            onClick = onGoogleClick
        )
    }
}

@Composable
fun DividerRow(
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HelpDivider(Modifier.weight(1f))
        Text(
            text = stringResource(textRes),
            style = HelpText,
            color = HelpColor,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.medium))
        )
        HelpDivider(Modifier.weight(1f))
    }
}

@Composable
fun HelpDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = HelpColor
    )
}

@Composable
fun EmailPasswordInput(
    modifier: Modifier = Modifier,
    uiState: AuthUiState,
    updateUiState: (AuthUiState) -> Unit,
    validEmail: () -> Boolean,
    validPassword: () -> Boolean,
    errorEmailRes: Int,
    errorPasswordRes: Int
) {
    Column(
        modifier = modifier
    ) {
        InputColumn(
            headlineText = stringResource(R.string.email),
            inputText = uiState.email,
            onValueChange = {
                updateUiState(uiState.copy(email = it))
            },
            validation = validEmail,
            errorTextRes = errorEmailRes,
            leadingIconRes = R.drawable.ic_usertag
        )
        InputColumn(
            headlineText = stringResource(R.string.password),
            inputText = uiState.password,
            onValueChange = {
                updateUiState(uiState.copy(password = it))
            },
            leadingIconRes = R.drawable.ic_lock,
            isHidden = true,
            trailingIconsRes = Pair(R.drawable.ic_eyeslash, R.drawable.ic_eye),
            imeAction = ImeAction.Done,
            validation = validPassword,
            errorTextRes = errorPasswordRes,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.medium))
        )
    }
}

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RectangleShape,
        border = BorderStroke(dimensionResource(R.dimen.mini), White),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.large))
    ) {
        Icon(
            painter = painterResource(R.drawable.google),
            contentDescription = null,
            tint = White
        )
        Text(
            text = "Google",
            style = GoogleText,
            color = White,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.small))
        )
    }
}