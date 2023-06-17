package com.example.senderos4.ui.register

import java.lang.Exception

sealed class RegisterUiStatus {

    object Resume : RegisterUiStatus()

    data class ErrorWithMessage(val message: String) : RegisterUiStatus()

    data class Error(val exception: Exception) : RegisterUiStatus()

    object Success : RegisterUiStatus()
}