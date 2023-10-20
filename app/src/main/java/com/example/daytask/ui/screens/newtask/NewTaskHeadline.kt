package com.example.daytask.ui.screens.newtask

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.daytask.ui.theme.NewTaskHeadlineText

@Composable
fun NewTaskHeadline(
    headlineText: String
) {
    Text(
        text = headlineText,
        style = NewTaskHeadlineText
    )
}