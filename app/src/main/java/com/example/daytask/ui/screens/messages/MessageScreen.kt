package com.example.daytask.ui.screens.messages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.DayTaskCenterTopAppBar
import com.example.daytask.navigation.NavigationDestination

object MessageDestination : NavigationDestination {
    override val route = "message"
    override val titleRes = R.string.messages
}

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = viewModel(),
    onBack: () -> Unit,
    navigateToUsers: () -> Unit,
    navigateToChat: (String) -> Unit
) {
    BackHandler(
        onBack = onBack,
        enabled = true
    )

    val chats by viewModel.chat.collectAsState()
    var editMode by remember { mutableStateOf(false) } /*TODO: UI and Delete chat*/

    Scaffold(
        topBar = {
            DayTaskCenterTopAppBar(
                currentRoute = MessageDestination.route,
                actions = {
                    IconButton(onClick = { editMode = !editMode }) {
                        val iconRes = if (editMode)
                            R.drawable.ic_close_square else R.drawable.ic_edit
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        MessageBody(
            navigateToUsers = navigateToUsers,
            navigateToChat = navigateToChat,
            chatList = chats,
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        )
    }
}