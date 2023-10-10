package com.example.daytask.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.HomeTopBar
import com.example.daytask.ui.theme.MainColor

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = 0 // ignore
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar(
                navigateToProfile = navigateToProfile,
                userName = user.displayName,
                userPhoto = user.photoUrl
            )
        }
    ) { paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Main Activity"
                )
                Text(
                    text = user.email!!,
                    color = MainColor
                )
            }
        }
    }
}
