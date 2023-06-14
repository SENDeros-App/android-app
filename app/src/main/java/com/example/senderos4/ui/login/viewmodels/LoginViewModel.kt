package com.example.senderos4.ui.login.viewmodels

import android.text.Spannable.Factory
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

class LoginViewModel(private val repository: LoginRepository):ViewModel() {

    var user = MutableLiveData("")
    var password = MutableLiveData("")

    private val _status = MutableLiveData<LoginUiStatus>(LoginUiStatus.Resume)
    val status: MutableLiveData<LoginUiStatus>
        get() = _status

    private fun login(user: String, password: String) {
        viewModelScope.launch {
            _status.postValue(
                when(val response = repository.login(user, password)){
                    is ApiResponse.Error -> LoginUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> LoginUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> LoginUiStatus.Success(response.data)
                }
            )
        }
    }

    fun onLogin() {
        if(!validateData()){
            _status.value = LoginUiStatus.ErrorWithMessage("Wrong information")
            return
        }
        login(user = user.value!!, password = password.value!!)
    }

    private fun validateData(): Boolean {
        when {
            user.value.isNullOrEmpty() -> return false
            password.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData() {
        user.value = ""
        password.value = ""
    }

    fun clearStatus() {
        _status.value = LoginUiStatus.Resume
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SenderosApplication
                LoginViewModel(app.credentialsRepository)
            }
        }

    }
}