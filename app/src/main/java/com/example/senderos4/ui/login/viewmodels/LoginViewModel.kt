package com.example.senderos4.ui.login.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.senderos4.SenderosApplication
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.ui.login.LoginUiStatus
import com.example.senderos4.ui.login.repositories.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var name = MutableLiveData("")
    var password = MutableLiveData("")

    private val _status = MutableLiveData<LoginUiStatus>(LoginUiStatus.Resume)
    val status: MutableLiveData<LoginUiStatus>
        get() = _status

    private fun login(name: String, password: String) {
        viewModelScope.launch {
            _status.postValue(
                when(val response = repository.login(name, password)){
                    is ApiResponse.Error -> {
                        LoginUiStatus.Error(response.exception)
                    }
                    is ApiResponse.ErrorWithMessage -> {
                        LoginUiStatus.ErrorWithMessage(response.message)
                    }
                    is ApiResponse.Success -> {
                        val token = response.data.first
                        val user = response.data.second
                        LoginUiStatus.Success(token, user)
                    }

                }
            )
        }
    }

    fun onLogin() {

        if(!validateData()){
            _status.value = LoginUiStatus.ErrorWithMessage("Por favor complete todos lo campos")
            return
        }

        login(name = name.value!!, password = password.value!!)
    }

    fun validateData(): Boolean {
        when {
            name.value.isNullOrEmpty() -> return false
            password.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData() {
        name.value = ""
        password.value = ""
    }

    fun clearStatus() {
        _status.value = LoginUiStatus.Resume
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SenderosApplication
                LoginViewModel(app.loginRepository)
            }
        }

    }
}