package com.example.senderos4.ui.register.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.senderos4.SenderosApplication
import com.example.senderos4.helpers.Validator
import com.example.senderos4.network.ApiResponse
import com.example.senderos4.ui.register.RegisterUiStatus
import com.example.senderos4.ui.register.repositories.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    var name = MutableLiveData("")
    var errorName = name.map { value ->
        val validator = Validator(value)
            .isRequired("El nombre es requerido")
            .minLength(4, "El nombre debe tener mas de 4 caracteres")
            .maxLength(50, "El nombre debe contener menos de 50 digitos")
            .matches(Regex("[a-zA-Z ]+"), "El nombre solamente puede estar compuesto por letras")
        val errors = validator.validate()
        if (errors.isEmpty())
            return@map ""
        else {
            return@map errors.joinToString(separator = "\n")
        }
    }

    var email = MutableLiveData("")
    var errorEmail = email.map { value ->
        val validator = Validator(value)
            .isRequired("El email es requerido")
            .matches(Regex("[a-zA-Z0-9@._-áéíóú]+"), "El email contiene digitos invalidos")
        val errors = validator.validate()
        if (errors.isEmpty())
            return@map ""
        else {
            return@map errors.joinToString(separator = "\n")
        }
    }

    var phone = MutableLiveData("")
    var errorPhoneNumber = phone.map { value ->
        val validator = Validator(value)
            .isRequired("El número es requerido")
            .minLength(8, "Ingresar un número valido")
        val errors = validator.validate()
        if (errors.isEmpty())
            return@map ""
        else {
            return@map errors.joinToString(separator = "\n")
        }
    }

    var user = MutableLiveData("")
    var errorUser = user.map { value ->
        val validator = Validator(value)
            .isRequired("Usuario es requerido")
            .matches(Regex("[a-z0-9._-]+"), "El email contiene digitos invalidos")
            .minLength(5, "El nombre de usuario debe contener como minimo 5 digitos")
            .maxLength(10, "EL nombre no debe exeder los 10 caracteres")
        val errors = validator.validate()
        if (errors.isEmpty())
            return@map ""
        else {
            return@map errors.joinToString(separator = "\n")
        }

    }

    var password = MutableLiveData("")
    var errorPassword = password.map { value ->
        val validator = Validator(value)
            .isRequired("Este campo es requerido")
            .minLength(8, "La contraseña debe tener minimo 8 digitos")
            .maxLength(15, "La contraseñ no debe exeder los 15 digitos")
            .matches(Regex("^[a-zA-Z0-9@#$%^&+=]+$"), "La contraseña contine digitos invalidos")
        val errors = validator.validate()
        if (errors.isEmpty())
            return@map ""
        else {
            return@map errors.joinToString(separator = "\n")
        }
    }


    var passwordConfirmation = MutableLiveData("")
    var errorPasswordConfirmation = passwordConfirmation.map {
        if (it != password.value) {
            return@map "Los password deben coincidir"
        } else {
            return@map ""
        }

    }

    var validatePhase1 = MediatorLiveData<Boolean>(false).apply {
        addSource(errorName) {
            value = it.isEmpty()
        }
        addSource(errorEmail) {
            value = it.isEmpty()
        }
        addSource(errorPhoneNumber) {
            value = it.isEmpty()
        }
    }


    var validatePhase2 = MediatorLiveData<Boolean>(false).apply {
        addSource(errorUser) {
            value = it.isEmpty()
        }
        addSource(errorPassword) {
            value = it.isEmpty()
        }
        /*addSource(errorPasswordConfirmation) {
            value = it.isEmpty()
        }*/
    }

    private val _status = MutableLiveData<RegisterUiStatus>(RegisterUiStatus.Resume)

    val status: LiveData<RegisterUiStatus>
        get() = _status

    private fun register(
        name: String,
        email: String,
        phone: String,
        user: String,
        password: String
    ) {
        viewModelScope.launch {
            _status.postValue(
                when (val response = registerRepository.register(
                    name = name,
                    email = email,
                    phone = phone,
                    user = user,
                    password = password
                )) {
                    is ApiResponse.Error -> RegisterUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> RegisterUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> RegisterUiStatus.Success
                }
            )
        }
    }

    fun orRegister() {
        register(
            name = name.value!!,
            email = email.value!!,
            password = password.value!!,
            user = user.value!!,
            phone = phone.value!!,
        )
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