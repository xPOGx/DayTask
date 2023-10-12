package com.example.daytask.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.daytask.R
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.theme.BottomBarColor
import com.example.daytask.ui.theme.BottomBarText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.Tertiary

@Composable
fun DayTaskBottomAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomBarState: Boolean,
    navigateToHome: () -> Unit,
    navigateToChat: () -> Unit,
    navigateToNewTask: () -> Unit,
    navigateToCalender: () -> Unit,
    navigateToNotification: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BottomAppBar(
            containerColor = Tertiary,
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomBarIcon(
                    onClick = navigateToHome,
                    iconRes = R.drawable.ic_home_2,
                    textRes = R.string.home,
                    active = currentRoute == HomeDestination.route,
                    modifier = Modifier.weight(1f)
                )
                BottomBarIcon(
                    onClick = navigateToChat,
                    iconRes = R.drawable.ic_messages_1,
                    textRes = R.string.chat,
                    active = currentRoute == MessageDestination.route,
                    modifier = Modifier.weight(1f)
                )
                AddIcon(
                    onClick = navigateToNewTask,
                    modifier = Modifier.weight(1f)
                )
                BottomBarIcon(
                    onClick = navigateToCalender,
                    iconRes = R.drawable.ic_calendar,
                    textRes = R.string.calendar,
                    active = currentRoute == CalendarDestination.route,
                    modifier = Modifier.weight(1f)
                )
                BottomBarIcon(
                    onClick = navigateToNotification,
                    iconRes = R.drawable.ic_notification_1,
                    textRes = R.string.notification,
                    active = currentRoute == NotificationDestination.route,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AddIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.clickable(
            onClick = onClick,
            indication = null,
            interactionSource = MutableInteractionSource()
        )
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.button_height))
                .background(MainColor)
        )
        Icon(
            painter = painterResource(R.drawable.ic_add_square),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
fun BottomBarIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    @StringRes textRes: Int,
    active: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                onClick = { if (!active) onClick() },
                indication = null,
                interactionSource = MutableInteractionSource()
            )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = if (active) MainColor else Color.Unspecified
        )
        Text(
            text = stringResource(textRes),
            style = BottomBarText,
            color = if (active) MainColor else BottomBarColor
        )
    }
}