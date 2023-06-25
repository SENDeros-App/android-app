package com.example.senderos4.ui.register.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.senderos4.SenderosApplication
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.ui.register.RegisterUiStatus
import com.example.senderos4.ui.register.repositories.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    var name = MutableLiveData("")
    var email = MutableLiveData("")
    var phone = MutableLiveData("")
    var user = MutableLiveData("")
    var password = MutableLiveData("")
    var passwordConfirmation = MutableLiveData("")
    var passwordMatch = MutableLiveData(true)

    private val _status = MutableLiveData<RegisterUiStatus>(RegisterUiStatus.Resume)

    val status: LiveData<RegisterUiStatus>
        get() = _status

    //para primer fragmento
    fun validateFields(): Boolean {
       when{
           name.value.isNullOrEmpty() -> return false
           email.value.isNullOrEmpty() -> return false
           phone.value.isNullOrEmpty() -> return false

       }
        return true
    }
///

    private fun register(name:String, email: String, phone:String, user:String, password:String){
       viewModelScope.launch {
           _status.postValue(
               when(val response = registerRepository.register(name = name, email = email, phone = phone, user = user, password = password)){
                   is ApiResponse.Error -> RegisterUiStatus.Error(response.exception)
                   is ApiResponse.ErrorWithMessage -> RegisterUiStatus.ErrorWithMessage(response.message)
                   is ApiResponse.Success -> RegisterUiStatus.Success
               }
           )
       }
   }

    fun orRegister() {
        if (!validateData()) {
            _status.value = RegisterUiStatus.ErrorWithMessage("Por favor, completa todos los campos correctamente")
            return
        }

        register(
            name = name.value!!,
            email = email.value!!,
            password = password.value!!,
            user = user.value!!,
            phone = phone.value!!,
        )
    }


    fun validateData(): Boolean {
        when {
            user.value.isNullOrEmpty() -> return false
            password.value.isNullOrEmpty() -> return false
            passwordConfirmation.value.isNullOrEmpty() -> return false
            password.value != passwordConfirmation.value -> {
                _status.value = RegisterUiStatus.ErrorWithMessage("Las contraseñas ingresadas no coinsiden")
                passwordMatch.value = password.value == passwordConfirmation.value
                return false
            }
        }
        return true
    }


    fun clearStatus() {
        _status.value = RegisterUiStatus.Resume
    }

    fun clearData() {
        name.value = ""
        email.value = ""
        phone.value = ""
        user.value = ""
        password.value = ""
        passwordConfirmation.value = ""
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SenderosApplication
                RegisterViewModel(app.registerRepository)
            }
        }
    }

}