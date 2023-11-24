package com.example.daytask.navigation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.daytask.R
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.chat.ChatDestination
import com.example.daytask.ui.screens.details.TaskDetailsNavigation
import com.example.daytask.ui.screens.edittask.EditTaskNavigation
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.newtask.NewTaskDestination
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.screens.profile.ProfileDestination
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.screens.tools.HorizontalAnimationText
import com.example.daytask.ui.screens.users.UsersDestination
import com.example.daytask.ui.theme.Background
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.MessageColor
import com.example.daytask.ui.theme.MessageUserNameText
import com.example.daytask.ui.theme.NavText
import com.example.daytask.ui.theme.UserNameText
import com.example.daytask.ui.theme.WelcomeText
import com.example.daytask.ui.theme.White

@Composable
fun DayTaskTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentRoute: String
) {
    DayTaskCenterTopAppBar(
        currentRoute = currentRoute,
        navigateUp = { navController.navigateUp() },
        actions = {
            if (currentRoute == CalendarDestination.route) {
                IconButton(onClick = { navController.navigate(NewTaskDestination.route) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_square),
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayTaskCenterTopAppBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    navigateUp: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            NavTitle(currentRoute = currentRoute)
        },
        navigationIcon = {
            NavIcon(
                navigateUp = navigateUp,
                currentRoute = currentRoute
            )
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Background
        ),
        modifier = modifier
    )
}

@Composable
fun NavIcon(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    currentRoute: String
) {
    when (currentRoute) {
        HomeDestination.route, MessageDestination.route, CalendarDestination.route,
        NotificationDestination.route -> {}

        else -> {
            IconButton(
                onClick = navigateUp,
                modifier = modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrowleft),
                    contentDescription = stringResource(R.string.back_button),
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun NavTitle(
    modifier: Modifier = Modifier,
    currentRoute: String
) {
    val titleRes = when (currentRoute) {
        HomeDestination.route -> HomeDestination.titleRes
        ProfileDestination.route -> ProfileDestination.titleRes
        MessageDestination.route -> MessageDestination.titleRes
        CalendarDestination.route -> CalendarDestination.titleRes
        NotificationDestination.route -> NotificationDestination.titleRes
        NewTaskDestination.route -> NewTaskDestination.titleRes
        TaskDetailsNavigation.routeWithArgs -> TaskDetailsNavigation.titleRes
        EditTaskNavigation.routeWithArgs -> EditTaskNavigation.titleRes
        UsersDestination.route -> UsersDestination.titleRes
        ChatDestination.routeWithArgs -> ChatDestination.titleRes
        else -> HomeDestination.titleRes
    }

    Text(
        text = stringResource(titleRes),
        style = NavText,
        modifier = modifier
    )
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    userName: String?,
    userPhoto: String?
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    photoUrl: String?,
    displayName: String?,
    isOnline: Boolean,
    call: () -> Unit,
    videoCall: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium)),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small))
            ) {
                AvatarImage(
                    userPhoto = photoUrl,
                    avatarSizeRes = R.dimen.image_small
                )
                Column {
                    Text(
                        text = displayName.toString(),
                        style = MessageUserNameText,
                        color = White,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                    Text(
                        text = if (isOnline)
                            stringResource(R.string.online) else stringResource(R.string.offline),
                        style = MessageUserNameText.copy(fontWeight = FontWeight.W400),
                        color = MessageColor
                    )
                }
            }
        },
        navigationIcon = {
            NavIcon(
                navigateUp = navigateUp,
                currentRoute = ChatDestination.routeWithArgs
            )
        },
        actions = {
            IconButton(onClick = call) {
                Icon(
                    painter = painterResource(R.drawable.ic_video),
                    contentDescription = null,
                    tint = White
                )
            }
            IconButton(onClick = videoCall) {
                Icon(
                    painter = painterResource(R.drawable.ic_call),
                    contentDescription = null,
                    tint = White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Background
        ),
        modifier = modifier
    )
}