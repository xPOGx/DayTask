package com.example.daytask

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.daytask.navigation.DayTaskNavHost

@Composable
fun DayTaskApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navigateToAuth: () -> Unit
) {
    DayTaskNavHost(
        navController = navController,
        navigateToAuth = navigateToAuth,
        modifier = modifier
    )
}
