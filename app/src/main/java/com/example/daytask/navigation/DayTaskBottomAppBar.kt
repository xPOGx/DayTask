package com.example.daytask.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.daytask.R
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.newtask.NewTaskDestination
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.screens.tools.SquareButton
import com.example.daytask.ui.theme.BottomBarColor
import com.example.daytask.ui.theme.BottomBarText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.Tertiary

@Composable
fun DayTaskBottomAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String
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
                onClick = { navController.popBackStack(HomeDestination.route, false) },
                iconRes = R.drawable.ic_home_2,
                textRes = R.string.home,
                active = currentRoute == HomeDestination.route,
                modifier = Modifier.weight(1f)
            )
            BottomBarIcon(
                onClick = { navController.navigate(MessageDestination.route) },
                iconRes = R.drawable.ic_messages_1,
                textRes = R.string.chat,
                active = currentRoute == MessageDestination.route,
                modifier = Modifier.weight(1f)
            )
            SquareButton(
                onClick = { navController.navigate(NewTaskDestination.route) },
                sizeRes = R.dimen.button_height,
                iconRes = R.drawable.ic_add_square,
                modifier = Modifier.weight(1f)
            )
            BottomBarIcon(
                onClick = { navController.navigate(CalendarDestination.route) },
                iconRes = R.drawable.ic_calendar,
                textRes = R.string.calendar,
                active = currentRoute == CalendarDestination.route,
                modifier = Modifier.weight(1f)
            )
            BottomBarIcon(
                onClick = { navController.navigate(NotificationDestination.route) },
                iconRes = R.drawable.ic_notification_1,
                textRes = R.string.notification,
                active = currentRoute == NotificationDestination.route,
                modifier = Modifier.weight(1f)
            )
        }
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