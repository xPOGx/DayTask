package com.example.daytask.ui.screens.newtask

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination

object NewTaskDestination : NavigationDestination {
    override val route = "new_task"
    override val titleRes = R.string.create_new_task
}

@Composable
fun NewTaskScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    BackHandler(
        onBack = onBack,
        enabled = true
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "NewTaskScreen")
    }
}