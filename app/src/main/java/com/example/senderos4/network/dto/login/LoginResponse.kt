package com.example.senderos4.network.dto.login

import com.example.senderos4.data.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("message")val message:String,
    @SerializedName("token")val token:String,
    @SerializedName("user")val user:User,
)