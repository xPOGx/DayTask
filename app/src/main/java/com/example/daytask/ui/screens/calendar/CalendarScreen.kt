package com.example.daytask.ui.screens.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.daytask.R
import com.example.daytask.navigation.NavigationDestination

object CalendarDestination : NavigationDestination {
    override val route = "calendar"
    override val titleRes = R.string.schedule
}

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    BackHandler(
        onBack = onBack,
        enabled = true
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "CalendarScreen")
    }
}