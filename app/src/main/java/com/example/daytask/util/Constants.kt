package com.example.daytask.util

import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import java.util.concurrent.TimeUnit

object Constants {
    const val PASSWORD_LENGTH = 6
    const val NAME_LENGTH = 3
    const val WEB_CLIENT_ID =
        "1097689850055-vtg3q678f856r7815kgmmofb704rd640.apps.googleusercontent.com"
    const val TIME_CHANGED = "time_changed"
    val noDismissProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        securePolicy = SecureFlagPolicy.SecureOn
    )
    val tenMinutesInMillis = TimeUnit.MINUTES.toMillis(10L)
}