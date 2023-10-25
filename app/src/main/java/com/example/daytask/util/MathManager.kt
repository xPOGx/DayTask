package com.example.daytask.util

import com.example.daytask.data.SubTask

object MathManager {
    fun countCompletePercentage(subTasksList: List<SubTask>): Float =
        if (subTasksList.isEmpty()) 0f
        else subTasksList.sumOf { it.completed.compareTo(false) } / subTasksList.size.toFloat()
}