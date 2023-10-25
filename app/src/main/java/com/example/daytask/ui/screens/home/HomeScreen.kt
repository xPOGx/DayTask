package com.example.daytask.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    navigateToDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeBody(
        uiState = uiState,
        navigateToDetails = navigateToDetails,
        updateQuery = viewModel::updateQuery,
        updateUiState = viewModel::updateUiState,
        modifier = modifier
    )
}
