package com.example.daytask.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.daytask.R
import com.example.daytask.ui.screens.calendar.CalendarDestination
import com.example.daytask.ui.screens.home.HomeDestination
import com.example.daytask.ui.screens.messages.MessageDestination
import com.example.daytask.ui.screens.newtask.NewTaskDestination
import com.example.daytask.ui.screens.notification.NotificationDestination
import com.example.daytask.ui.screens.tools.CircleCanvas
import com.example.daytask.ui.screens.tools.SquareButton
import com.example.daytask.ui.theme.Background
import com.example.daytask.ui.theme.BottomBarColor
import com.example.daytask.ui.theme.BottomBarText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.PlaceholderColor
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.util.TextFieldManager
import com.example.daytask.util.TextFieldManager.clearFocusOnKeyboardDismiss

@Composable
fun DayTaskBottomAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentRoute: String,
    isNotify: Boolean
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
                redDot = isNotify,
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
    active: Boolean,
    redDot: Boolean = false
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (active) MainColor else Color.Unspecified
            )
            if (redDot) {
                CircleCanvas(
                    radius = 10f,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
        Text(
            text = stringResource(textRes),
            style = BottomBarText,
            color = if (active) MainColor else BottomBarColor
        )
    }
}

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    newMessageText: String,
    onValueChange: (String) -> Unit,
    sendFile: () -> Unit,
    sendMessage: () -> Unit,
    sendVoice: () -> Unit
) {
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    BottomAppBar(
        containerColor = Background,
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.small))
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = newMessageText,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.type_a_message),
                        color = PlaceholderColor
                    )
                },
                colors = TextFieldManager.colors(Tertiary),
                shape = RectangleShape,
                leadingIcon = {
                    IconButton(onClick = sendFile) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add_file),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = sendMessage) {
                        Icon(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .onSizeChanged { if (textFieldSize.height == 0) textFieldSize = it }
                    .clearFocusOnKeyboardDismiss()
                    .weight(1f)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size((textFieldSize.height / LocalDensity.current.density).dp)
                    .background(Tertiary)
            ) {
                IconButton(onClick = sendVoice) {
                    Icon(
                        painter = painterResource(R.drawable.ic_microphone_2),
                        contentDescription = null,
                        tint = MainColor
                    )
                }
            }
        }
    }
}