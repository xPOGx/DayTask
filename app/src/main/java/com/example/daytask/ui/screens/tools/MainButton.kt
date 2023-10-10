package com.example.daytask.ui.screens.tools

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import com.example.daytask.R
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.ButtonText
import com.example.daytask.ui.theme.White

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        shape = RectangleShape,
        enabled = enabled,
        modifier = modifier.height(dimensionResource(R.dimen.button_height))
    ) {
        Text(
            text = text,
            style = ButtonText,
            color = if (enabled) Black else White
        )
    }
}
