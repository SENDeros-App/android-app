package com.example.senderos4.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.example.senderos4.databinding.FragmentRegister2Binding
import com.example.senderos4.ui.register.viewmodels.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import retrofit2.HttpException


class Register2Fragment : Fragment() {

    private lateinit var binding: FragmentRegister2Binding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModel.Factory
    }

    companion object {
        private const val ERROR_MESSAGE_PASSWORD_MISMATCH = "Las contraseñas no coinciden"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegister2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStatus()
        setViewModel()
        clearError()
    }

    private fun observeStatus() {
        registerViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }

        registerViewModel.passwordMatch.observe(viewLifecycleOwner) { passwordMatch ->
            handlePasswordMatch(passwordMatch)
        }
    }

    private fun handlePasswordMatch(passwordMatch: Boolean) {
        val passwordTextInputLayouts = listOf(
            binding.textInputLayoutPassword,
            binding.textInputLayoutPassword2
        )

        if (!passwordMatch) {
            passwordTextInputLayouts.forEach { textInputLayout ->
                setErrorText(textInputLayout, ERROR_MESSAGE_PASSWORD_MISMATCH)
            }
        } else {
            passwordTextInputLayouts.forEach { textInputLayout ->
                textInputLayout.error = null
            }
        }
    }

    private fun clearError() {
        val passwordTextInputLayouts = listOf(
            binding.textInputLayoutPassword,
            binding.textInputLayoutPassword2
        )
        passwordTextInputLayouts.forEach { textInputLayout ->
            textInputLayout.clearErrorOnFocusChange()
        }
    }

    fun TextInputLayout.clearErrorOnFocusChange() {
        editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                error = null
            }
        }
    }

    private fun setErrorText(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.error = errorMessage
        textInputLayout.clearFocus()
    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun handleUiStatus(status: RegisterUiStatus) {
        when (status) {
            is RegisterUiStatus.Error -> {
                if (status.exception is HttpException) {
                    when (status.exception.code()) {
                        500 ->
                            Toast.makeText(
                                requireContext(),
                                "Error al conectarse al servidor ...",
                                Toast.LENGTH_SHORT
                            ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error has occurred",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Log.d("Error Login", "error", status.exception)
            }

            is RegisterUiStatus.ErrorWithMessage -> {
                registerViewModel.clearStatus()
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
                when (status.message) {
                    "email ya registrado " -> navigateToRegisterFragment("email", "El email es inválido")
                    "Usuario ya registrado " -> navigateToRegisterFragment("userName", "El nombre de usuario es inválido")
                    "telefonico ya registrado" -> navigateToRegisterFragment("phoneNumber", "El número de teléfono es inválido")
                }
            }

            is RegisterUiStatus.Success -> {
                registerViewModel.clearStatus()
                registerViewModel.clearData()
                findNavController().navigate(R.id.action_register2Fragment_to_loginFragment)
            }

            else -> {}
        }
    }

    private fun navigateToRegisterFragment(errorField: String, errorMessage: String) {
        val bundle = bundleOf("errorField" to errorField, "errorMessage" to errorMessage)
        findNavController().navigate(R.id.action_register2Fragment_to_registerFragment, bundle)
    }
}

