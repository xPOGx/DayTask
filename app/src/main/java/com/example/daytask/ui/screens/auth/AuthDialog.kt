package com.example.daytask.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.util.Constants.noDismissProperties
import com.example.daytask.ui.screens.tools.MainDivider

@Composable
fun AuthDialog(
    modifier: Modifier = Modifier,
    dialogText: String,
    dialogTitle: String,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = noDismissProperties
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            AuthDialogBody(
                dialogText = dialogText,
                dialogTitle = dialogTitle,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun AuthDialogBody(
    modifier: Modifier = Modifier,
    dialogText: String,
    dialogTitle: String,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = dimensionResource(R.dimen.big),
                vertical = dimensionResource(R.dimen.big)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium))
    ) {
        Text(
            text = dialogTitle,
            style = MaterialTheme.typography.titleLarge
        )
        MainDivider()
        Text(dialogText)
        Button(
            onClick = onDismissRequest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.i_have_read, dialogTitle),
                textAlign = TextAlign.Center
            )
        }
    }
}
