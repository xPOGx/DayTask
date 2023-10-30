package com.example.daytask.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.ui.screens.tools.MainButton
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.PlaceholderColor
import com.example.daytask.util.TextFieldManager

@Composable
fun DetailDialog(
    onDismissRequest: () -> Unit,
    title: String,
    validTitle: Boolean,
    updateTitle: (String) -> Unit,
    addSubTask: () -> Unit
) {
    Dialog(onDismissRequest) {
        AddSubTask(
            title = title,
            validTitle = validTitle,
            updateTitle = updateTitle,
            addSubTask = {
                addSubTask()
                onDismissRequest()
            },
            cancel = onDismissRequest
        )
    }
}

@Composable
fun AddSubTask(
    modifier: Modifier = Modifier,
    title: String,
    validTitle: Boolean,
    updateTitle: (String) -> Unit,
    addSubTask: () -> Unit,
    cancel: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column {
            OutlinedTextField(
                value = title,
                onValueChange = updateTitle,
                placeholder = {
                    Text(
                        text = stringResource(R.string.enter_subtask_title),
                        style = HelpText,
                        color = PlaceholderColor
                    )
                },
                shape = RectangleShape,
                colors = TextFieldManager.colors(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (validTitle) addSubTask() }),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.medium))
                    .fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                MainButton(
                    onClick = cancel,
                    text = stringResource(R.string.cancel),
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.medium))
                )
                MainButton(
                    onClick = addSubTask,
                    text = stringResource(R.string.save),
                    enabled = validTitle
                )
            }
        }
    }
}