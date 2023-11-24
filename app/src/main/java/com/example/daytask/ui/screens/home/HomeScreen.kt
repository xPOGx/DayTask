package com.example.daytask.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.navigation.HomeTopBar
import com.example.daytask.navigation.NavigationDestination
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    navigateToDetails: (String) -> Unit,
    navigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = remember { Firebase.auth.currentUser!! }

    Scaffold(
        topBar = {
            HomeTopBar(
                navigateToProfile = navigateToProfile,
                userName = user.displayName,
                userPhoto = user.photoUrl.toString(),
                modifier = modifier
                    .padding(horizontal = dimensionResource(R.dimen.big))
                    .padding(bottom = dimensionResource(R.dimen.big))
            )
        },
        modifier = modifier
    ) { paddingValues ->
        HomeBody(
            uiState = uiState,
            navigateToDetails = navigateToDetails,
            updateQuery = viewModel::updateQuery,
            updateUiState = viewModel::updateUiState,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
