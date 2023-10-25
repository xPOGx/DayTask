package com.example.daytask.ui.screens.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.daytask.R
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.InputText
import com.example.daytask.ui.theme.PlaceholderColor
import com.example.daytask.ui.theme.White
import com.example.daytask.util.TextFieldManager
import com.example.daytask.util.TextFieldManager.clearFocusOnKeyboardDismiss

@Composable
fun DisabledField(
    modifier: Modifier = Modifier,
    value: String,
    placeholderText: String = "",
    @DrawableRes leadingIconRes: Int,
    @DrawableRes trailingIconRes: Int = R.drawable.ic_edit_profile,
    changerField: () -> Unit,
    disabled: Boolean = false
) {
    val trailingIconView = @Composable {
        IconButton(onClick = changerField) {
            Icon(
                painter = painterResource(trailingIconRes),
                contentDescription = null
            )
        }
    }
    val supportingTextView = @Composable {
        Text(
            text = stringResource(R.string.not_be_changed),
            style = HelpText,
            color = HelpColor
        )
    }

    OutlinedTextField(
        value = value,
        onValueChange = { /* ignore */ },
        textStyle = InputText.copy(White),
        colors = TextFieldManager.colors(),
        shape = RectangleShape,
        placeholder = {
            Text(
                text = placeholderText,
                style = HelpText.copy(PlaceholderColor)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
            )
        },
        trailingIcon = if (!disabled) trailingIconView else null,
        supportingText = if (disabled) supportingTextView else null,
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .clearFocusOnKeyboardDismiss()
    )
}