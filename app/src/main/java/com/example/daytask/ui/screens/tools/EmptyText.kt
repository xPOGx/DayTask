package com.example.daytask.ui.screens.tools

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.daytask.R

@Composable
fun EmptyText(
    modifier: Modifier = Modifier
) {
    /*TODO Nice empty Box*/
    Text(
        text = stringResource(R.string.empty),
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}