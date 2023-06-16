package com.example.senderos4.network.dto.login

import com.example.senderos4.data.User
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("msg")val message:String,
    @SerializedName("token")val token:String
    //val user:User
)