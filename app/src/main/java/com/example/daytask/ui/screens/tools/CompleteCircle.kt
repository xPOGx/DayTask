package com.example.daytask.ui.screens.tools

import androidx.annotation.DimenRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.daytask.R
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.PercentageText
import com.example.daytask.ui.theme.White

@Composable
fun CompleteCircle(
    modifier: Modifier = Modifier,
    @DimenRes sizeRes: Int,
    percentage: Float
) {
    val valueInt by animateIntAsState(
        targetValue = (percentage * 100).toInt(),
        label = "textAnimation"
    )
    val valueFloat by animateFloatAsState(
        targetValue = percentage,
        label = "drawArcAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(dimensionResource(sizeRes))
    ) {
        Canvas(modifier = Modifier.size(dimensionResource(sizeRes))) {
            drawArc(
                color = MainColor,
                startAngle = -30f,
                sweepAngle = -360f * valueFloat,
                useCenter = false,
                style = Stroke(
                    width = 8f,
                    cap = StrokeCap.Round
                )
            )
        }
        Text(
            text = stringResource(R.string.percentage, valueInt),
            style = PercentageText,
            color = White
        )
    }
}