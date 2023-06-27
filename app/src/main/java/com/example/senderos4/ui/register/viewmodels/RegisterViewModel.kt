package com.example.senderos4.ui.register.viewmodels

import android.util.Patterns
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
    val userError: MutableLiveData<String> = MutableLiveData()
    val passwordError: MutableLiveData<String> = MutableLiveData()


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

    fun validateName(name: String): Boolean {
        val regex = Regex("[a-zA-Z ]+")
        return name.length >= 2 && regex.matches(name)
    }

    fun validateEmail(email: String): Boolean {
        val regex = Regex("[a-zA-Z0-9@._-]+")
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && regex.matches(email)
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
        val userRegex = Regex("^[a-zA-Z0-9]+$")
        val passwordRegex = Regex("^[a-zA-Z0-9@#$%^&+=]+$")

        when {
            user.value.isNullOrEmpty() -> {
                userError.value = "El campo de nombre no puede estar vacío"
                return false
            }
            !user.value!!.matches(userRegex) -> {
                userError.value = "Digitos invalidos"
                return false
            }
            password.value.isNullOrEmpty() -> {
                passwordError.value = "El campo de contraseña no puede estar vacío"
                return false
            }
            !password.value!!.matches(passwordRegex) -> {
                passwordError.value = "El campo de contraseña solo puede contener letras, números y los siguientes símbolos: @ # $ % ^ & + ="
                return false
            }
            passwordConfirmation.value.isNullOrEmpty() -> return false
            password.value != passwordConfirmation.value -> {
                _status.value = RegisterUiStatus.ErrorWithMessage("Las contraseñas ingresadas no coinciden")
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