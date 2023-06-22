package com.example.senderos4.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.example.senderos4.databinding.FragmentRegisterBinding
import com.example.senderos4.ui.register.viewmodels.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import retrofit2.HttpException


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModel.Factory
    }

    companion object {
        private const val ERROR_MESSAGE_EMPTY_FIELD = "Por favor, completa este campo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        click()
        setViewModel()
        clearError()
        identError()

    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    fun identError() {
        val errorField = arguments?.getString("errorField")
        val errorMessage = arguments?.getString("errorMessage")
        if (!errorField.isNullOrEmpty() && !errorMessage.isNullOrEmpty()) {
            when (errorField) {
                "email" -> binding.textInputLayoutEmail.error = errorMessage
                "userName" -> binding.textInputLayoutUser.error = errorMessage
                "phoneNumber" -> binding.textInputLayoutPhone.error = errorMessage
            }
        }
    }



    private fun click() {
        binding.confirmationInfo.setOnClickListener {
            registerViewModel.apply {
                name.value = binding.textUserName.text.toString()
                email.value = binding.textUseremail.text.toString()
                phone.value = binding.textUserPhoneNumbre.text.toString()
            }

            val fieldsAreValid = registerViewModel.validateFields()
            if (fieldsAreValid) {
                findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
            } else {
                val textInputLayouts = listOf(
                    binding.textInputLayoutUser,
                    binding.textInputLayoutEmail,
                    binding.textInputLayoutPhone
                )
                textInputLayouts.forEach { textInputLayout ->
                    setErrorText(textInputLayout, ERROR_MESSAGE_EMPTY_FIELD)
                }
            }
        }
    }


    private fun clearError() {
        val textInputLayouts = listOf(
            binding.textInputLayoutUser,
            binding.textInputLayoutEmail,
            binding.textInputLayoutPhone
        )
        textInputLayouts.forEach { textInputLayout ->
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

}

