package com.example.senderos4.network.dto.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("msg") val message:String
    )