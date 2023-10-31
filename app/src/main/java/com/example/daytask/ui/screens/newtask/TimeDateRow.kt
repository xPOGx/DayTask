package com.example.daytask.ui.screens.newtask

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.daytask.R
import com.example.daytask.ui.theme.DateText
import com.example.daytask.ui.theme.HelpColor
import com.example.daytask.ui.theme.HelpText
import com.example.daytask.ui.theme.MainColor
import com.example.daytask.ui.theme.Secondary
import com.example.daytask.ui.theme.Tertiary
import com.example.daytask.ui.theme.White
import com.example.daytask.util.Constants.TIME_CHANGED
import com.example.daytask.util.Constants.timeLimit
import com.example.daytask.util.Constants.timeLimitMillis
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
    var timeChecked by remember { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

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
        initialSelectedDateMillis = currentDate + tempCalendar.timeZone.getOffset(currentDate),
        yearRange = currentYear..currentYear + 100
    )

    timeCheck(
        Calendar.getInstance().also { it.timeInMillis += timeLimitMillis },
        currentDate,
        timeState,
        dateState,
        { timeState = it },
        { saveDate(it) }
    )
    if (timeChecked) {
        timeCheck(
            Calendar.getInstance().also { it.timeInMillis += timeLimitMillis },
            currentDate,
            timeState,
            dateState,
            { timeState = it },
            { saveDate(it) }
        )
        timeChecked = false
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = "checkTime") {
        val timeChangedBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                if (action != null && action == TIME_CHANGED) {
                    timeChecked = true
                }
            }
        }
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(timeChangedBroadcast, IntentFilter(TIME_CHANGED))
    }

    var dateShow by remember { mutableStateOf(false) }
    var timeShow by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small)),
        modifier = modifier
    ) {
        NewTaskHeadline(headlineText = headlineText)
        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.small))) {
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
                dateText = DateFormatter.formatDate(currentDate),
                onClick = { dateShow = true },
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = stringResource(R.string.minimum_time_limit_minutes, timeLimit),
            style = HelpText,
            color = HelpColor
        )
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

@OptIn(ExperimentalMaterial3Api::class)
private fun timeCheck(
    calendar: Calendar,
    uiTime: Long,
    timeState: TimePickerState,
    dateState: DatePickerState,
    updateTimeState: (TimePickerState) -> Unit,
    saveDate: (Long) -> Unit
) {
    val selectedDate = dateState.selectedDateMillis
    val selectedTime = if (selectedDate != null) selectedDate -
            TimeZone.getDefault().getOffset(selectedDate) +
            TimeUnit.HOURS.toMillis(timeState.hour.toLong()) +
            TimeUnit.MINUTES.toMillis(timeState.minute.toLong())
    else uiTime

    val selectedMin = TimeUnit.MILLISECONDS.toMinutes(selectedTime)
    val currentMin = TimeUnit.MILLISECONDS.toMinutes(calendar.timeInMillis)
    val uiMin = TimeUnit.MILLISECONDS.toMinutes(uiTime)
    when {
        selectedMin < currentMin -> {
            val currentTime = calendar.timeInMillis
            saveDate(currentTime)
            updateTimeState(
                TimePickerState(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
            )
            dateState.setSelection(currentTime)
        }

        selectedMin != uiMin ->
            saveDate(selectedTime)
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