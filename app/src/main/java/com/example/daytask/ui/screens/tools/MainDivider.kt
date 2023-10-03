package com.example.daytask.ui.screens.tools

import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.daytask.R
import com.example.daytask.ui.theme.MainColor

@Composable
fun MainDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        thickness = dimensionResource(R.dimen.mini),
        color = MainColor
    )
}