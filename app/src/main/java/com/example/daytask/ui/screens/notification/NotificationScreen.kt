package com.example.daytask.ui.screens.notification

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination

object NotificationDestination : NavigationDestination {
    override val route = "notification"
    override val titleRes = R.string.notifications
}

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = viewModel(),
    onBack: () -> Unit,
    navigateToChat: (String) -> Unit
) {
    BackHandler(
        onBack = onBack,
        enabled = true
    )

    val uiState by viewModel.uiState.collectAsState()

    NotificationBody(
        notificationsList = uiState.notificationList,
        action = { notification ->
            val key = notification.action.first
            val id = notification.action.second

            when (key) {
                "message" -> {
                    navigateToChat(id)
                    val tempDelete = uiState.notificationList.filter {
                        it.action.first == key && it.action.second == id
                    }
                    tempDelete.forEach { viewModel.removeNotification(it.id) }
                }

                "task" -> {
                    viewModel.removeNotification(id)
                }

                else -> { viewModel.removeNotification(id) }
            }

        },
        modifier = modifier
    )
}