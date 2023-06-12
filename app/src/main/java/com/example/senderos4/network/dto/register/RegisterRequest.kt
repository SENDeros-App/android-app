package com.example.senderos4.network.dto.register

data class RegisterRequest (
    val name:String,
    val email:String,
    val phoneNumber:String,
    val user:String,
    val password:String,
    )