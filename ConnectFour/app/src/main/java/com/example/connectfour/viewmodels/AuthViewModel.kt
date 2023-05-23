package com.example.connectfour.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectfour.models.User
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var currentUser: User? = null
}