package com.example.daytask.navigation

import androidx.compose.animation.animateContentSize
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
    var bottomBarState by remember { mutableStateOf(false) }
    var topBarState by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var currentItemId by remember { mutableStateOf("") }

    when (navBackStackEntry?.destination?.route) {
        HomeDestination.route, MessageDestination.route, CalendarDestination.route,
        NotificationDestination.route -> {
            topBarState = true
            bottomBarState = true
        }

        ProfileDestination.route, NewTaskDestination.route, TaskDetailsNavigation.routeWithArgs,
        EditTaskNavigation.routeWithArgs -> {
            topBarState = true
            bottomBarState = false
        }

        else -> {
            topBarState = false
            bottomBarState = false
        }
    }

    Scaffold(
        topBar = {
            DayTaskTopAppBar(
                navController = navController,
                topBarState = topBarState,
                currentItemId = currentItemId
            )
        },
        bottomBar = {
            DayTaskBottomAppBar(
                navController = navController,
                bottomBarState = bottomBarState,
                navigateToHome = { navController.popBackStack(HomeDestination.route, false) },
                navigateToChat = { navController.navigate(MessageDestination.route) },
                navigateToNewTask = { navController.navigate(NewTaskDestination.route) },
                navigateToCalender = { navController.navigate(CalendarDestination.route) },
                navigateToNotification = { navController.navigate(NotificationDestination.route) }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier
                .padding(paddingValues)
                .animateContentSize()
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
                EditTaskScreen()
            }
        }
    }
}
