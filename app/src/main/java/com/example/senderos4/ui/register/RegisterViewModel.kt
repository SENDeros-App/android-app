package com.example.senderos4.ui.register

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
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {

    private val _status = MutableLiveData<RegisterUiStatus>(RegisterUiStatus.Resume)
    val status: LiveData<RegisterUiStatus>
        get() = _status

    var name = MutableLiveData("")
    var errorName = MediatorLiveData("").apply { ->
        addSource(name){nameValue ->
            val validator = Validator(nameValue)
                .isRequired("El nombre es requerido")
                .minLength(4, "El nombre debe tener mas de 4 caracteres")
                .maxLength(50, "El nombre debe contener menos de 50 digitos")
                .matches(
                    Regex("^[a-zA-ZÀ-ÿ\\s]+$"),
                    "El nombre solamente puede estar compuesto por letras"
                )
            val errors = validator.validate()
            value = if(errors.isEmpty()) ""
            else {
                errors.joinToString(separator = "\n")
            }
        }
    }

    var email = MutableLiveData("")
    var errorEmail = MediatorLiveData("").apply {

        var emailValidate = false

        addSource(email){emailValue ->
            val validator = Validator(emailValue)
                .isRequired("El email es requerido")
                .matches(Regex("^([a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})$"), "El email contiene digitos invalidos")
            val errors = validator.validate()
            value = if (errors.isEmpty()) {
                emailValidate = false
                ""
            }
            else {
                errors.joinToString(separator = "\n")
            }
        }

        addSource(_status) { status ->
            value = when {
                !emailValidate -> {
                    "El email es requerido" // Agrega el mensaje de error correspondiente
                }
                status is RegisterUiStatus.ErrorWithMessage && status.message == "email ya registrado " -> {
                    "Email ya registrado "
                }
                else -> ""
            }
        }
    }

    var phone = MutableLiveData("")
    var errorPhoneNumber = MediatorLiveData("").apply {

        var phoneValidate = false

        addSource(phone){phoneValue ->
            val validator = Validator(phoneValue)
                .isRequired("El número de teléfono es requerido")
                .minLength(8, "Ingresar un número valido")
            val errors = validator.validate()
            value = if (errors.isEmpty()) {
                phoneValidate = true
                ""
            }
            else {
                errors.joinToString(separator = "\n")
            }
        }


        addSource(_status) { status ->
            value = when {
                !phoneValidate -> {
                    "El número de teléfono es requerido" // Agrega el mensaje de error correspondiente
                }
                status is RegisterUiStatus.ErrorWithMessage && status.message == "telefonico ya registrado" -> {
                    "Numero telefonico ya registrado "
                }
                else -> ""
            }
        }
    }


    var user = MutableLiveData("")
    var errorUser = MediatorLiveData("").apply {

        var userValidate = false

        addSource(user) { userValue ->
            val validator = Validator(userValue)
                .isRequired("El nombre de suario es requerido")
                .matches(Regex("[a-z0-9._-]+"), "El nombre de usuario solo debe contener letras minúsculas, dígitos y los caracteres especiales . _ -")
                .minLength(5, "El nombre de usuario debe contener como minimo 5 digitos")
                .maxLength(10, "EL nombre no debe exeder los 10 caracteres")
            val errors = validator.validate()
            value = if (errors.isEmpty()) {
                userValidate = true
                ""
            }
            else {
                errors.joinToString(separator = "\n")
            }
        }

        addSource(_status) { status ->
            value = when {
                !userValidate -> {
                    "El nombre de suario es requerido" // Agrega el mensaje de error correspondiente
                }
                status is RegisterUiStatus.ErrorWithMessage && status.message == "Usuario ya registrado " -> {
                    "Usuario ya registrado "
                }
                else -> ""
            }
        }

        /*addSource(_status) {
            value = when (it) {
                is RegisterUiStatus.ErrorWithMessage -> {
                    if (it.message == "Usuario ya registrado ") {
                        "Usuario ya registrado "
                    } else {
                        ""
                    }
                }
                else -> ""
            }
        }*/
    }

    var password = MutableLiveData("")
    var errorPassword = MediatorLiveData("").apply {
        addSource(password){passwordValue ->
            val validator = Validator(passwordValue)
                .isRequired("Este campo es requerido")
                .minLength(8, "La contraseña debe tener minimo 8 digitos")
                .maxLength(15, "La contraseñ no debe exeder los 15 digitos")
                .matches(Regex("^[a-zA-Z0-9@#$%^&+=-_]+$"), "La contraseña contine digitos invalidos")
            val errors = validator.validate()
            value = if(errors.isEmpty()) ""
            else{
                errors.joinToString(separator = "\n")
            }
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

    var validatePhase1 = MediatorLiveData(false).apply {
        addSource(errorName) {
            val errorValuePhone = errorPhoneNumber.value?.isEmpty() ?: false
            val errorValueEmail = errorEmail.value?.isEmpty() ?: false
            value = errorValueEmail && errorValuePhone && it.isEmpty()
        }

        addSource(errorEmail) {
            val errorValueName = errorName.value?.isEmpty() ?: false
            val errorPhoneNumber = errorPhoneNumber.value?.isEmpty() ?: false
            value = errorPhoneNumber && errorValueName && it.isEmpty()
        }

        addSource(errorPhoneNumber) {
            val errorValueName = errorName.value?.isEmpty() ?: false
            val errorValueEmail = errorEmail.value?.isEmpty() ?: false
            value = errorValueEmail && errorValueName && it.isEmpty()
        }
    }

    var validatePhase2 = MediatorLiveData(false).apply {
        addSource(errorUser) {
            val errorValuePassword = errorPassword.value?.isEmpty() ?: false
            val errorPasswordConfirmation = errorPasswordConfirmation.value?.isEmpty() ?: false
            value = errorValuePassword && errorPasswordConfirmation && it.isEmpty()
        }

        addSource(errorPassword) {
            val errorValueUser = errorUser.value?.isEmpty() ?: false
            val errorPasswordConfirmation = errorPasswordConfirmation.value?.isEmpty() ?: false
            value = errorValueUser && errorPasswordConfirmation && it.isEmpty()
        }

        addSource(errorPasswordConfirmation) {
            val errorValueUser = errorUser.value?.isEmpty() ?: false
            val errorValuePassword = errorPassword.value?.isEmpty() ?: false
            value = errorValueUser && errorValuePassword && it.isEmpty()
        }
    }

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