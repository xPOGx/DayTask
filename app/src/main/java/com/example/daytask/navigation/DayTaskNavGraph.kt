package com.example.daytask.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.calendar.CalendarScreen
import com.example.daytask.ui.screens.details.TaskDetailsNavigation
import com.example.daytask.ui.screens.details.TaskDetailsScreen
import com.example.daytask.ui.screens.edittask.EditTaskNavigation
import com.example.daytask.ui.screens.edittask.EditTaskScreen
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.home.HomeScreen
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.messages.MessageScreen
import com.example.daytask.ui.screens.newtask.NewTaskDestination
import com.example.daytask.ui.screens.newtask.NewTaskScreen
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.screens.notification.NotificationScreen
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
    var bottomBarState by remember { mutableStateOf(true) }
    var currentItemId by remember { mutableStateOf("") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: HomeDestination.route

    bottomBarState = when (currentRoute) {
        HomeDestination.route, MessageDestination.route, CalendarDestination.route,
        NotificationDestination.route -> true

        else -> false
    }

    Scaffold(
        topBar = {
            DayTaskTopAppBar(
                navController = navController,
                currentRoute = currentRoute,
                currentItemId = currentItemId
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarState,
                enter = slideInVertically { it } + expandVertically(),
                exit = shrinkVertically() + slideOutVertically { it }
            ) {
                DayTaskBottomAppBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = HomeDestination.route) {
                HomeScreen(
                    navigateToDetails = { id ->
                        currentItemId = id
                        navController.navigate("${TaskDetailsNavigation.route}/$id")
                    }
                )
            }
            composable(route = ProfileDestination.route) {
                ProfileScreen(
                    signOut = {
                        Firebase.auth.signOut()
                        navigateToAuth()
                    },
                    navigateUp = { navController.navigateUp() }
                )
            }
            composable(route = MessageDestination.route) {
                MessageScreen(
                    onBack = { navController.popBackStack(HomeDestination.route, false) }
                )
            }
            composable(route = CalendarDestination.route) {
                CalendarScreen(
                    onBack = { navController.popBackStack(HomeDestination.route, false) }
                )
            }
            composable(route = NotificationDestination.route) {
                NotificationScreen(
                    onBack = { navController.popBackStack(HomeDestination.route, false) }
                )
            }
            composable(route = NewTaskDestination.route) {
                NewTaskScreen(
                    navigateUp = { navController.navigateUp() }
                )
            }
            composable(
                route = TaskDetailsNavigation.routeWithArgs,
                arguments = listOf(navArgument(TaskDetailsNavigation.taskId) {
                    type = NavType.StringType
                })
            ) {
                TaskDetailsScreen(navigateUp = { navController.navigateUp() })
            }
            composable(
                route = EditTaskNavigation.routeWithArgs,
                arguments = listOf(navArgument(EditTaskNavigation.taskId) {
                    type = NavType.StringType
                })
            ) {
                EditTaskScreen(navigateUp = { navController.navigateUp() })
            }
        }
    }
}
