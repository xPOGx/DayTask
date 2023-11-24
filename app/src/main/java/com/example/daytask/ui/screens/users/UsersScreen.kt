package com.example.daytask.ui.screens.users

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.daytask.R
import com.example.daytask.navigation.DayTaskCenterTopAppBar
import com.example.daytask.navigation.NavigationDestination
import com.example.daytask.ui.theme.White

object UsersDestination : NavigationDestination {
    override val route = "users"
    override val titleRes = R.string.new_message
}

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateToUserChat: (String) -> Unit
) {
    Scaffold(
        topBar = {
            DayTaskCenterTopAppBar(
                currentRoute = UsersDestination.route,
                navigateUp = navigateUp,
                actions = {
                    IconButton(onClick = { /*TODO: search people by name*/ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        UsersBody(
            navigateToUserChat = {
                navigateUp()
                navigateToUserChat(it)
            },
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding() + dimensionResource(R.dimen.medium))
                .padding(horizontal = dimensionResource(R.dimen.big))
        )
    }
}