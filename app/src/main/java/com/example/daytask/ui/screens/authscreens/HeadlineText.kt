package com.example.daytask.ui.screens.authscreens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.daytask.ui.theme.HeadlineText
import com.example.daytask.ui.theme.White

@Composable
fun HeadlineText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        color = White,
        style = HeadlineText,
        modifier = modifier
    )
}