package com.example.daytask.ui.screens.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.ChatBottomBar
import com.example.daytask.navigation.ChatTopBar
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.util.AppViewModelProvider

object ChatDestination : NavigationDestination {
    override val route = "chat"
    override val titleRes = R.string.chat
    const val userId = "userId"
    val routeWithArgs = "$route/{$userId}"
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ChatTopBar(
                navigateUp = navigateUp,
                photoUrl = uiState.userPhoto,
                displayName = uiState.userName,
                isOnline = uiState.isOnline,
                call = { /*TODO: Call feature*/ },
                videoCall = { /*TODO: VideoCall feature*/ }
            )
        },
        bottomBar = {
            ChatBottomBar(
                newMessageText = uiState.newMessageText,
                onValueChange = { viewModel.updateUiState(uiState.copy(newMessageText = it)) },
                sendFile = { /*TODO: select file in system*/ },
                sendMessage = viewModel::sendMessage,
                sendVoice = { /*TODO: Send voice message*/ }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        ChatBody(
            messagesList = uiState.messageList,
            modifier = Modifier.padding(paddingValues)
        )
    }
}