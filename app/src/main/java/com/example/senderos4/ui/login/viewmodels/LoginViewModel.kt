package com.example.senderos4.ui.login.viewmodels

import android.text.Spannable.Factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.senderos4.SenderosApplication
import com.example.senderos4.ui.login.repositories.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository):ViewModel() {

    fun loginUser(userName:String, userPassword:String):Boolean{
        val user  = loginRepository.validateData(userName, userPassword)
        return user != null
    }

    companion object{
        val Factory = viewModelFactory {
            initializer {
                val repository =(this[APPLICATION_KEY] as SenderosApplication).loginRepository
                LoginViewModel(repository)
            }
        }
    }
}