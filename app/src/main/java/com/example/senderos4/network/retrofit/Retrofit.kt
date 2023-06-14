package com.example.senderos4.network.retrofit

import com.example.senderos4.network.service.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL = "https://retrofit.up.railway.app/"

object RetrofitInstance {

    private var token = ""

    fun setToken(token: String){
        this.token = token
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getLoginService(): AuthService{
        return retrofit.create(AuthService::class.java)
    }

}