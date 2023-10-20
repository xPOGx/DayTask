package com.example.daytask.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    fun formatTime(time: Int): String = String.format("%02d", time)
    fun formatDate(date: Long): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

    fun formatShortDate(date: Long): String =
        SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
}
