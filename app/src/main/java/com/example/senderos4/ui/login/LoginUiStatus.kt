package com.example.senderos4.ui.login

import com.example.senderos4.data.User
import java.lang.Exception

sealed class LoginUiStatus {
    object Resume : LoginUiStatus()
    class Error(val exception: Exception) : LoginUiStatus()
    data class ErrorWithMessage(val message: String) : LoginUiStatus()
    data class Success(val token: String, val user: User) : LoginUiStatus()
}