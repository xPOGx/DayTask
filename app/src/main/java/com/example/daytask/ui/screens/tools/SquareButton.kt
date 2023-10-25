package com.example.daytask.ui.screens.tools

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.daytask.ui.theme.MainColor

@Composable
fun SquareButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DimenRes sizeRes: Int,
    @DrawableRes iconRes: Int
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(MainColor)
            .size(dimensionResource(sizeRes))
    ) {
        Icon(
            painter = painterResource(iconRes),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}