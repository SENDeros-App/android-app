package com.example.senderos4.network.dto.login

import com.example.senderos4.data.User

data class LoginData(
    val token: String,
    val user: User
)