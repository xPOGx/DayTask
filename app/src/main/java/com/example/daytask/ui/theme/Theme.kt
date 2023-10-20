package com.example.daytask.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val mainColorScheme = darkColorScheme(
    primary = MainColor,
    secondary = Secondary,
    background = Background,
    tertiary = Tertiary,
    onSurface = White
)

@Composable
fun DayTaskTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = mainColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}