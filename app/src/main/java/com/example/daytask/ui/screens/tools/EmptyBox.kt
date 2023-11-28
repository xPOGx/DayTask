package com.example.daytask.ui.screens.tools

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White

@Composable
fun EmptyBox(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = screenWidth / (screenWidth / 100)
    val padding = dimensionResource(R.dimen.big).value * 2
    val availableWidth = (screenWidth - padding - cardWidth) * density

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val value by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = availableWidth,
        animationSpec = infiniteRepeatable(
            tween(5000, 0) { it },
            RepeatMode.Reverse
        ),
        label = "ping pong"
    )

    Box(modifier.fillMaxWidth()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Tertiary),
            modifier = Modifier
                .width(cardWidth.dp)
                .offset { IntOffset(value.toInt(), 0) }
        ) {
            Text(
                text = stringResource(R.string.empty),
                textAlign = TextAlign.Center,
                color = White,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small))
                    .fillMaxWidth()
            )
        }
    }
}