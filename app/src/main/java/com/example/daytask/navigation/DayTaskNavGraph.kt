package com.example.daytask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.home.HomeScreen
import com.example.daytask.ui.screens.profile.ProfileDestination
import com.example.daytask.ui.screens.profile.ProfileScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun DayTaskNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigateToAuth: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToProfile = { navController.navigate(ProfileDestination.route) }
            )
        }
        composable(route = ProfileDestination.route) {
            ProfileScreen(
                signOut = {
                    Firebase.auth.signOut()
                    navigateToAuth()
                },
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
