package com.example.senderos4

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.senderos4.data.User
import com.example.senderos4.data.headers
import com.example.senderos4.data.users
import com.example.senderos4.network.retrofit.RetrofitInstance
import com.example.senderos4.network.retrofit.RetrofitInstance.setUser
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository
import com.example.senderos4.ui.login.repositories.LoginRepository
import com.example.senderos4.ui.register.repositories.RegisterRepository

class SenderosApplication : Application() {


    private val prefs: SharedPreferences by lazy {
        getSharedPreferences("Retrofit", Context.MODE_PRIVATE)
    }


    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val USER_DIVISION = "user_division"
    }

    //new

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    fun saveUser(user: User) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, user.name)
        editor.putString(USER_DIVISION, user.division)
        editor.apply()
        _user.value = user
    }


    fun getUser(): User? {
        val userName = prefs.getString(USER_NAME, null)
        val userDivision = prefs.getString(USER_DIVISION, null)
        val user = if (userName != null && userDivision != null) User(userName, userDivision) else null
        _user.value = user
        return user
    }




    //

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
        editor.remove(USER_NAME)
        editor.remove(USER_DIVISION)
        editor.apply()

        checkLoggedInStatus()
    }

    fun checkLoggedInStatus() {
        val token = getTokent()
        _isLoggedIn.value = token.isNotEmpty() && true
    }

    //


    /*val senderosApplication = application as SenderosApplication
    senderosApplication.clearAuthToken()*/


    private fun getAPIService() = with(RetrofitInstance) {
        setToken(getTokent())
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