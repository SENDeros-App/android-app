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

class SenderosApplication:Application() {

    //new
    fun isLoggedIn(): Boolean {
        val token = getTokent()
        return token.isNotEmpty()
    }
    //

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    /*val senderosApplication = application as SenderosApplication
    senderosApplication.clearAuthToken()*/



    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("Retrofit", Context.MODE_PRIVATE)
    }

    private fun getAPIService() = with(RetrofitInstance) {
        setToken(getTokent())
        getLoginService()
    }

    fun getTokent(): String = prefs.getString(USER_TOKEN, "")!!

    val credentialsRepository: LoginRepository by lazy {
        LoginRepository(getAPIService())
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }


    companion object {
        const val USER_TOKEN = "user_token"
    }

    val classificationRepository: ClassificationRepository by lazy {
        ClassificationRepository(users, headers)
    }
}