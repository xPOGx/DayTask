package com.example.daytask.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import com.example.daytask.ui.theme.Secondary

object TextFieldManager {
    @Composable
    fun colors(containerColor: Color = Secondary) = TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        focusedLeadingIconColor = Color.Unspecified,
        unfocusedLeadingIconColor = Color.Unspecified,
        focusedTrailingIconColor = Color.Unspecified,
        unfocusedTrailingIconColor = Color.Unspecified,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
        var isFocused by remember { mutableStateOf(false) }
        var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
        if (isFocused) {
            val imeIsVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
            val focusManager = LocalFocusManager.current
            LaunchedEffect(imeIsVisible) {
                if (imeIsVisible) {
                    keyboardAppearedSinceLastFocused = true
                } else if (keyboardAppearedSinceLastFocused) {
                    focusManager.clearFocus()
                }
            }
        }
        onFocusEvent {
            if (isFocused != it.isFocused) {
                isFocused = it.isFocused
                if (isFocused) {
                    keyboardAppearedSinceLastFocused = false
                }
            }
        }
    }
}