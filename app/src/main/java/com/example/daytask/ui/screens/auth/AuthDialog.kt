package com.example.daytask.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.MainDivider

enum class DialogReason {
    Loading,
    Privacy
}

@Composable
fun AuthDialog(
    modifier: Modifier = Modifier,
    reason: DialogReason,
    dialogText: String = "",
    dialogTitle: String = "",
    onDismissRequest: () -> Unit = { /* ignore */ },
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false

) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            securePolicy = SecureFlagPolicy.SecureOn
        )
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            when (reason) {
                DialogReason.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.large))
                    )

                else -> {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(
                                horizontal = dimensionResource(R.dimen.big),
                                vertical = dimensionResource(R.dimen.big)
                            )
                        ,
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
            }
        }
    }
}