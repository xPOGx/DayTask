package com.example.daytask.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.data.Task
import com.example.daytask.ui.theme.TaskTitleText
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White

@Composable
fun MyTaskDialogs(
    onDismissRequest: () -> Unit,
    tasksList: List<Task>,
    deleteTask: (String) -> Unit,
    navigateToNewTask: () -> Unit
) {
    if (tasksList.isEmpty()) {
        AlertNoTasks(
            onDismissRequest = onDismissRequest,
            navigateToNewTask = navigateToNewTask
        )
    } else {
        DeleteTaskDialog(
            onDismissRequest = onDismissRequest,
            tasksList = tasksList,
            deleteTask = deleteTask
        )
    }
}

@Composable
fun AlertNoTasks(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    navigateToNewTask: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.oh_god_no_tasks))
        },
        text = {
            Text(text = stringResource(R.string.fix_error))
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    navigateToNewTask()
                }
            ) {
                Text(text = stringResource(R.string.god_create))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.nah_nothing))
            }
        },
        modifier = modifier
    )
}

@Composable
fun DeleteTaskDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    deleteTask: (String) -> Unit,
    tasksList: List<Task>
) {
    Dialog(onDismissRequest) {
        Column(modifier) {
            Text(
                text = stringResource(R.string.be_careful_task_removal),
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Tertiary)
                    .fillMaxWidth()
            )
            AllTasksListDelete(
                tasksList = tasksList,
                deleteTask = {
                    if (tasksList.size == 1) onDismissRequest()
                    deleteTask(it)
                }
            )
        }
    }
}

@Composable
fun AllTasksListDelete(
    modifier: Modifier = Modifier,
    tasksList: List<Task>,
    deleteTask: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = dimensionResource(R.dimen.medium))
            .height((LocalConfiguration.current.screenHeightDp / 2).dp)
    ) {
        items(
            items = tasksList,
            key = { it.id }
        ) { task ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Tertiary
                ),
                shape = RectangleShape,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(dimensionResource(R.dimen.medium))
                ) {
                    Text(
                        text = task.title,
                        style = TaskTitleText,
                        color = White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { deleteTask(task.id) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_trash),
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            }
        }
    }
}