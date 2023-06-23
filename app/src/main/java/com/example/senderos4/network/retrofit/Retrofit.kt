package com.example.senderos4.network.retrofit

import com.example.senderos4.data.User
import com.example.senderos4.network.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://10.149.13.101:5000/api/"

object RetrofitInstance {

    private var token = ""
    private lateinit var user: User

    fun setToken(token: String){
        this.token = token
    }

    fun setUser(user: User){
        this.user = user
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): AuthService{
        return retrofit.create(AuthService::class.java)
    }

}