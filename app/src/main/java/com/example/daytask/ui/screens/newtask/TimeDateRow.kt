package com.example.daytask.ui.screens.newtask

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.example.daytask.R
import com.example.daytask.ui.theme.DateText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.Secondary
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White
import com.example.daytask.util.Constants
import com.example.daytask.util.DateFormatter
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDateRow(
    modifier: Modifier = Modifier,
    headlineText: String,
    currentDate: Long,
    saveDate: (Long) -> Unit
) {
    val calendar = Calendar.getInstance().also { it.timeInMillis += Constants.tenMinutesInMillis }
    val currentTime = calendar.timeInMillis
    val currentYear = calendar.get(Calendar.YEAR)

    val tempCalendar = Calendar.getInstance().also { it.timeInMillis = currentDate }
    var timeState by remember {
        mutableStateOf(
            TimePickerState(
                initialHour = tempCalendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = tempCalendar.get(Calendar.MINUTE),
                is24Hour = true
            )
        )
    }

    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate,
        yearRange = currentYear..currentYear + 100
    )

    val date = dateState.selectedDateMillis
    val selectedDate: Long =
        if (date != null) date -
                TimeZone.getDefault().getOffset(date) +
                TimeUnit.HOURS.toMillis(timeState.hour.toLong()) +
                TimeUnit.MINUTES.toMillis(timeState.minute.toLong())
        else currentDate

    if (selectedDate > currentDate) saveDate(selectedDate)

    val badTime =
        TimeUnit.MILLISECONDS.toMinutes(selectedDate) < TimeUnit.MILLISECONDS.toMinutes(currentTime)
    if (badTime) {
        dateState.setSelection(currentTime)
        timeState = TimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            is24Hour = true
        )
        saveDate(currentTime)
    }

    var dateShow by remember { mutableStateOf(false) }
    var timeShow by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
        modifier = modifier
    ) {
        NewTaskHeadline(headlineText = headlineText)
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small))
        ) {
            DateCard(
                drawableRes = R.drawable.ic_clock,
                dateText = stringResource(
                    R.string.time_format,
                    DateFormatter.formatTime(timeState.hour),
                    DateFormatter.formatTime(timeState.minute)
                ),
                onClick = { timeShow = true },
                modifier = Modifier.weight(1f)
            )
            DateCard(
                drawableRes = R.drawable.ic_calendar_new_task,
                dateText = DateFormatter.formatDate(selectedDate),
                onClick = { dateShow = true },
                modifier = Modifier.weight(1f)
            )
        }
    }
    if (timeShow) {
        TimeDialog(
            onDismissRequest = { timeShow = false },
            timeState = timeState
        )
    }
    if (dateShow) {
        DatePickerDialog(
            onDismissRequest = { dateShow = false },
            confirmButton = {
                Button(onClick = { dateShow = false }) {
                    Text(text = stringResource(R.string.confirm))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}

@Composable
fun DateCard(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    dateText: String,
    onClick: () -> Unit
) {
    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Secondary),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            DateIcon(drawableRes = drawableRes)
            Text(
                text = dateText,
                textAlign = TextAlign.Center,
                style = DateText.copy(White),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DateIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(MainColor)
            .size(dimensionResource(R.dimen.image_small))
    ) {
        Icon(
            painter = painterResource(drawableRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    timeState: TimePickerState,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timeState,
                colors = TimePickerDefaults.colors(
                    timeSelectorSelectedContainerColor = Tertiary,
                    timeSelectorSelectedContentColor = MainColor
                )
            )
            Button(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}