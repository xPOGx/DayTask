package com.example.daytask.util

import java.text.DateFormatSymbols
import java.util.Calendar

object CalendarManager {
    /**
     * Calendar instance
     */
    private val calendar = Calendar.getInstance()

    /**
     * Current month in range 0-11
     */
    private val currentMonth: Int = calendar.get(Calendar.MONTH)

    /**
     * Current month string
     */
    val currentMonthString: String = DateFormatSymbols().months[currentMonth]

    /**
     * Current day of month in range 1-(28..31)
     */
    val currentDayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)

    /**
     * Current month range 1-(28..31)
     */
    fun getMonthDayRange(): IntRange =
        1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    /**
     * Current day of week, first three characters
     * @param day of current month
     */
    fun getDayOfWeekShort(day: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.DAY_OF_MONTH, day)
        val dayString = DateFormatSymbols().weekdays[c.get(Calendar.DAY_OF_WEEK)]
        return dayString.substring(0, 3)
    }

    /**
     * Long range for [day] from 00:00 to 23:59
     * @param day of current month
     */
    fun getDateRangeForDay(day: Int): LongRange {
        val c = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
        }
        val start = c.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        val end = c.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis
        return start..end
    }

    /**
     * Long date for [day] of current month
     * @param day of current month
     */
    fun getDayDate(day: Int): Long {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }
}