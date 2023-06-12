package com.example.senderos4.network.dto.login

import com.example.senderos4.data.User

data class LoginResponse (
    val token:String,
    val message:String,
    val user: User
    )