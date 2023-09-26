package com.example.daytask.ui.screens.tools

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

@Composable
fun MultiColorText(
    textWithColors: Array<Pair<String, Color>>,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            textWithColors.forEach { (text, color) ->
                withStyle(style = SpanStyle(color = color)) {
                    append(text)
                }
            }
        },
        overflow = TextOverflow.Visible,
        style = style,
        modifier = modifier
    )
}
