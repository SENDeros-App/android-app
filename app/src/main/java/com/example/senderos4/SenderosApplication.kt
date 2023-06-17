package com.example.senderos4

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.senderos4.data.headers
import com.example.senderos4.data.users
import com.example.senderos4.network.retrofit.RetrofitInstance
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository
import com.example.senderos4.ui.login.repositories.LoginRepository
import com.example.senderos4.ui.register.repositories.RegisterRepository

class SenderosApplication:Application() {

    //new

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    override fun onCreate() {
        super.onCreate()
        checkLoggedInStatus()
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
        checkLoggedInStatus()
    }

    fun getTokent(): String {
        return prefs.getString(USER_TOKEN, "") ?: ""
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
        checkLoggedInStatus()
    }

    fun checkLoggedInStatus() {
        val token = getTokent()
        _isLoggedIn.value = token.isNotEmpty()
    }

    //


    /*val senderosApplication = application as SenderosApplication
    senderosApplication.clearAuthToken()*/



    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("Retrofit", Context.MODE_PRIVATE)
    }

    private fun getAPIService() = with(RetrofitInstance) {
        setToken(getTokent())
        getLoginService()
    }


    val loginRepository: LoginRepository by lazy {
        LoginRepository(getAPIService())
    }

    val registerRepository:RegisterRepository by lazy{
        RegisterRepository(getAPIService())
    }


    companion object {
        const val USER_TOKEN = "user_token"
    }

    val classificationRepository: ClassificationRepository by lazy {
        ClassificationRepository(users, headers)
    }
}