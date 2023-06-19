package com.example.senderos4.network.dto.register

data class RegisterRequest (
    val username:String,
    val email:String,
    val password:String,
    val name:String,
    val phoneNumber:String,
    )