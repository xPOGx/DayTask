package com.example.daytask.ui.screens.tools

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.ButtonText

@Composable
fun MainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = ButtonText,
            color = Black
        )
    }
}
