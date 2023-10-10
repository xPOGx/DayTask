package com.example.daytask.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.daytask.R
import com.example.daytask.navigation.DayTaskNavHost
import com.example.daytask.ui.screens.tools.AvatarImage
import com.example.daytask.ui.theme.Background
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.NavText
import com.example.daytask.ui.theme.UserNameText
import com.example.daytask.ui.theme.WelcomeText

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayTaskTopAppBar(
    modifier: Modifier = Modifier,
    titleRes: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = NavText
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrowleft),
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Background
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navigateToProfile: () -> Unit,
    userName: String?,
    userPhoto: Uri?
) {
    Row(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                text = "Welcome Back!",
                style = WelcomeText,
                color = MainColor
            )
            Text(
                text = userName ?: "",
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
