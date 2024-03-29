package com.example.senderos4.network.service

import android.net.Credentials
import com.example.senderos4.network.dto.login.LoginRequest
import com.example.senderos4.network.dto.login.LoginResponse
import com.example.senderos4.network.dto.register.RegisterRequest
import com.example.senderos4.network.dto.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signin")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @POST("auth/signup")
    suspend fun register(@Body credentials: RegisterRequest):RegisterResponse
}