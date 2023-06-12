package com.example.senderos4.network.service

import android.net.Credentials
import com.example.senderos4.network.dto.login.LoginRequest
import com.example.senderos4.network.dto.login.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("")
    suspend fun login(@Body credentials: LoginRequest):LoginResponse
}