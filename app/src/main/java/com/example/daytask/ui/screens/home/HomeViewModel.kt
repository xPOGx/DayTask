package com.example.daytask.ui.screens.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {
    private val _user = MutableStateFlow(Firebase.auth.currentUser!!)
    val user = _user.asStateFlow()
}