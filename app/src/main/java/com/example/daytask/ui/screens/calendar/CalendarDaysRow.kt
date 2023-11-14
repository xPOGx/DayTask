package com.example.daytask.ui.screens.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import com.example.daytask.R
import com.example.daytask.ui.theme.Black
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.MonthText
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.WeekText
import com.example.daytask.ui.theme.White
import com.example.daytask.util.CalendarManager

@Composable
fun CalendarDaysRow(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    selectedDay: Int,
    dayClicked: (Int) -> Unit
) {
    LazyRow(
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium)),
        modifier = modifier
    ) {
        items(
            items = CalendarManager.getMonthDayRange().toList(),
            key = { it }
        ) {
            DateCard(
                number = it,
                selected = it == selectedDay,
                onClick = { dayClicked(it) }
            )
        }
    }
}

@Composable
fun DateCard(
    modifier: Modifier = Modifier,
    number: Int,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val textColor = if (selected) Black else White
    val containerColor = if (selected) MainColor else Tertiary

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RectangleShape,
        modifier = modifier
            .width(dimensionResource(R.dimen.date_card_width))
            .height(dimensionResource(R.dimen.date_card_height))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = number.toString(),
                    style = MonthText,
                    color = textColor
                )
                Text(
                    text = CalendarManager.getDayOfWeekShort(number),
                    style = WeekText,
                    color = textColor
                )
            }
        }
    }
}