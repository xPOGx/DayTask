package com.example.daytask.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    /**
     * Convert [time] to two char string with 0 at start if [time] < 2 digits
     * @param time integer value
     */
    fun formatTime(time: Int): String = String.format("%02d", time)

    /**
     * Convert [date] to string day/month/year as 01/01/1970
     * @param date time in millis
     */
    fun formatDate(date: Long): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

    /**
     * Convert [date] to string day/month as 1 January
     * @param date time in millis
     */
    fun formatShortDate(date: Long): String =
        SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)

    /**
     * Convert [date] to string hourOfDay:minute as 00:00, 23:59
     * @param date time in millis
     */
    fun formatTimeFromDate(date: Long): String =
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
}
