package com.example.daytask.ui.screens.tools

import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.daytask.R
import com.example.daytask.ui.theme.MainColor

@Composable
fun AddIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DimenRes sizeRes: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = MutableInteractionSource()
            )
            .size(dimensionResource(sizeRes))
            .background(MainColor)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add_square),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}