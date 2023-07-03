package com.example.senderos4

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.senderos4.data.User
import com.example.senderos4.data.headers
import com.example.senderos4.data.users
import com.example.senderos4.network.dto.login.LoginData
import com.example.senderos4.network.retrofit.RetrofitInstance
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository
import com.example.senderos4.ui.login.repositories.LoginRepository
import com.example.senderos4.ui.register.RegisterRepository

class SenderosApplication : Application() {
    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("Retrofit", Context.MODE_PRIVATE)
    }

    private val _loginData = MutableLiveData<LoginData?>()
    val loginData: LiveData<LoginData?>
        get() = _loginData

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val USER_DIVISION = "user_division"
    }

    override fun onCreate() {
        super.onCreate()
        checkLoggedInStatus()
    }

    fun saveLoginData(loginData: LoginData) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, loginData.token)
        editor.putString(USER_NAME, loginData.user.name)
        editor.putString(USER_DIVISION, loginData.user.division)
        editor.apply()
        _loginData.value = loginData
    }

    fun clearLoginData() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_NAME)
        editor.remove(USER_DIVISION)
        editor.apply()
        _loginData.value = null
    }

    private fun checkLoggedInStatus() {
        val loginData = loadLoginData()
        _loginData.value = loginData
    }

    private fun loadLoginData(): LoginData? {
        val token = prefs.getString(USER_TOKEN, null)
        val userName = prefs.getString(USER_NAME, null)
        val userDivision = prefs.getString(USER_DIVISION, null)
        val user = if (userName != null && userDivision != null) User(userName, userDivision) else null
        return if (token != null && user != null) LoginData(token, user) else null
    }

    private fun getAPIService() = with(RetrofitInstance){
        loadLoginData()?.let { setLoginData(it) }
        getLoginService()
    }



    val loginRepository: LoginRepository by lazy {
        LoginRepository(getAPIService())
    }

    val registerRepository: RegisterRepository by lazy {
        RegisterRepository(getAPIService())
    }

    val classificationRepository: ClassificationRepository by lazy {
        ClassificationRepository(users, headers)
    }
}
