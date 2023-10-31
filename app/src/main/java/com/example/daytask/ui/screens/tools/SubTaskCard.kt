package com.example.daytask.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import com.example.daytask.R
import com.example.daytask.data.SubTask
import com.example.daytask.ui.screens.details.DetailTitleTextBox
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.Secondary

@Composable
fun SubTaskCard(
    modifier: Modifier = Modifier,
    subtask: SubTask,
    actionSubTask: () -> Unit,
    editMode: Boolean = false,
    showDialog: () -> Unit = {}
) {
    val iconRes = when {
        editMode -> R.drawable.ic_trash
        subtask.completed -> R.drawable.ic_tick_circle
        else -> R.drawable.ic_notick_circle
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Secondary),
        shape = RectangleShape,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailTitleTextBox(
                text = subtask.title,
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.medium))
                    .padding(start = dimensionResource(R.dimen.medium))
                    .weight(1f)
            )
            if (editMode) {
                SquareButton(
                    onClick = showDialog,
                    sizeRes = R.dimen.button_height,
                    iconRes = R.drawable.ic_edit,
                    tint = Black
                )
            }
            SquareButton(
                onClick = actionSubTask,
                sizeRes = R.dimen.button_height,
                iconRes = iconRes,
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.small))
                    .padding(end = dimensionResource(R.dimen.small))
            )
        }
    }
}