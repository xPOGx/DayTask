package com.example.daytask.util

import android.content.Context
import android.widget.Toast
import com.example.daytask.R
import com.google.android.gms.tasks.Task

object NotifyManager {
    fun notifyUser(
        context: Context,
        message: String = context.getString(R.string.no_internet)
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun <T> notifyUser(task: Task<T>, context: Context) {
        val text = if (task.isSuccessful)
            context.getString(R.string.successful) else task.exception?.message.toString()
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}