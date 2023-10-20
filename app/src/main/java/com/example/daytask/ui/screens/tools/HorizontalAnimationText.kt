package com.example.daytask.ui.screens.tools

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.example.daytask.R
import com.example.daytask.ui.theme.UserNameText
import com.example.daytask.util.TextWidthManager.calculateTextWidth
import kotlinx.coroutines.delay

@Composable
fun HorizontalAnimationText(
    scrollState: ScrollState,
    text: String?,
    style: TextStyle
) {
    if (text.isNullOrEmpty()) return

    val density = LocalDensity.current
    val textWidth = calculateTextWidth(text, UserNameText, LocalContext.current, density)
    val width = LocalConfiguration.current.screenWidthDp - dimensionResource(R.dimen.big).value * 2
    val textAnimateKey = textWidth > width
    if (textAnimateKey) {
        val second = 1000
        val multiplier = textWidth / (width / density.density)
        val duration = (second * multiplier).toInt()
        LaunchedEffect(key1 = true) {
            val myTween = tween<Float>(
                durationMillis = duration,
                easing = { it }
            )
            while (true) {
                if (scrollState.value == 0)
                    scrollState.animateScrollTo(scrollState.maxValue, myTween)
                else
                    scrollState.animateScrollTo(0, myTween)
                delay(second.toLong())
            }
        }
    }

    Text(
        text = text,
        style = style,
        modifier = Modifier.horizontalScroll(scrollState)
    )
}