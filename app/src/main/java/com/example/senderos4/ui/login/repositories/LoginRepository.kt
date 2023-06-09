package com.example.senderos4.ui.login.repositories

import com.example.senderos4.data.User

class LoginRepository(private val users:List<User>) {

    fun validateData(userName:String, userPassword:String):User?{
        return users.find { it.name ==userName && it.password == userPassword }
    }
}