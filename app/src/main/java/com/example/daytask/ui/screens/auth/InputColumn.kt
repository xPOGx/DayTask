package com.example.daytask.ui.screens.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.daytask.R
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.InputText
import com.example.daytask.ui.theme.PlaceholderColor
import com.example.daytask.ui.theme.Secondary
import com.example.daytask.ui.theme.White

@Composable
fun InputColumn(
    modifier: Modifier = Modifier,
    headlineText: String,
    inputText: String,
    imeAction: ImeAction = ImeAction.Next,
    isHidden: Boolean = false,
    onValueChange: (String) -> Unit,
    @DrawableRes leadingIconRes: Int,
    @DrawableRes trailingIconsRes: Pair<Int, Int>? = null,
    @StringRes errorTextRes: Int,
    validation: () -> Boolean
) {
    var hidden by remember {
        mutableStateOf(isHidden)
    }
    var isError by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val keyboardActions = if (imeAction == ImeAction.Next)
        KeyboardActions.Default else KeyboardActions(
        onDone = {
            focusManager.clearFocus()
        }
    )
    val keyboardOptions = KeyboardOptions.Default.copy(
        imeAction = imeAction,
        keyboardType = if (imeAction == ImeAction.Next)
            KeyboardType.Email else KeyboardType.Password
    )

    Column(
        modifier = modifier
    ) {
        Text(
            text = headlineText,
            style = InputText,
            color = HelpColor,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
        )
        OutlinedTextField(
            value = inputText,
            onValueChange = {
                onValueChange(it)
                isError = !validation()
            },
            textStyle = InputText.copy(White),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Secondary,
                unfocusedContainerColor = Secondary,
                focusedLeadingIconColor = White,
                unfocusedLeadingIconColor = White,
                focusedTrailingIconColor = White,
                unfocusedTrailingIconColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RectangleShape,
            placeholder = {
                Text(
                    text = stringResource(R.string.enter, headlineText.lowercase()),
                    style = HelpText.copy(PlaceholderColor)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(leadingIconRes),
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (trailingIconsRes != null) {
                    IconButton(onClick = { hidden = !hidden }) {
                        Icon(
                            painter = if (hidden) {
                                painterResource(trailingIconsRes.first)
                            } else painterResource(trailingIconsRes.second),
                            contentDescription = null
                        )
                    }
                }
            },
            visualTransformation = if (hidden) {
                PasswordVisualTransformation()
            } else VisualTransformation.None,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(errorTextRes),
                        style = HelpText,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}