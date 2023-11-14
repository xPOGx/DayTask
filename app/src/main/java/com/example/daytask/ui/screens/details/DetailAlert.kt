package com.example.daytask.ui.screens.details

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.daytask.R

@Composable
fun DetailAlert(
    title: String,
    taskComplete: Boolean,
    completePercentage: Float,
    finishTask: () -> Unit,
    closeAlert: () -> Unit,
) {
    val alertText = if (taskComplete)
        stringResource(R.string.activate) else stringResource(R.string.finish)
    val helpText = if (!taskComplete && completePercentage > 0f && completePercentage < 1f)
        stringResource(R.string.uncompleted_subtasks) else ""
    AlertDialog(
        onDismissRequest = closeAlert,
        confirmButton = {
            Button(
                onClick = {
                    closeAlert()
                    finishTask()
                }
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = closeAlert) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = stringResource(R.string.complete_task, alertText, helpText).trim())
        }
    )
}