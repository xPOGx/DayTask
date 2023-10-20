package com.example.daytask.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.daytask.R
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.details.TaskDetailsNavigation
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.newtask.NewTaskDestination
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.screens.profile.ProfileDestination
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.screens.tools.HorizontalAnimationText
import com.example.daytask.ui.theme.Background
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.NavText
import com.example.daytask.ui.theme.UserNameText
import com.example.daytask.ui.theme.WelcomeText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayTaskTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topBarState: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val titleRes: Int = chooseTitle(navBackStackEntry?.destination?.route ?: HomeDestination.route)

    AnimatedVisibility(
        visible = topBarState,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        if (navBackStackEntry?.destination?.route == HomeDestination.route) {
            val user = Firebase.auth.currentUser!!
            HomeTopBar(
                navigateToProfile = { navController.navigate(ProfileDestination.route) },
                userName = user.displayName,
                userPhoto = user.photoUrl,
                modifier = Modifier.padding(dimensionResource(R.dimen.big))
            )
        } else {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(titleRes),
                        style = NavText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack(HomeDestination.route, false) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrowleft),
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Background
                ),
                modifier = modifier
            )
        }
    }
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    userName: String?,
    userPhoto: Uri?
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = dimensionResource(R.dimen.small))
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                style = WelcomeText,
                color = MainColor
            )
            HorizontalAnimationText(
                scrollState = rememberScrollState(),
                text = userName,
                style = UserNameText
            )
        }
        AvatarImage(
            onImageClick = navigateToProfile,
            avatarSizeRes = R.dimen.image_small,
            userPhoto = userPhoto
        )
    }
}

private fun chooseTitle(route: String): Int = when (route) {
    HomeDestination.route -> HomeDestination.titleRes
    ProfileDestination.route -> ProfileDestination.titleRes
    MessageDestination.route -> MessageDestination.titleRes
    CalendarDestination.route -> CalendarDestination.titleRes
    NotificationDestination.route -> NotificationDestination.titleRes
    NewTaskDestination.route -> NewTaskDestination.titleRes
    TaskDetailsNavigation.routeWithArgs -> TaskDetailsNavigation.titleRes
    else -> HomeDestination.titleRes
}
