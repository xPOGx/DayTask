package com.example.daytask.ui.screens.tools

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CircleCanvas(
    modifier: Modifier = Modifier,
    radius: Float,
    color: Color
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            radius = radius
        )
    }
}