package com.example.daytask.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daytask.DayTaskApplication

object AppViewModelProvider {

}

fun CreationExtras.dayTaskApplication(): DayTaskApplication =
    (this[APPLICATION_KEY] as DayTaskApplication)