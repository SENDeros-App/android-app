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

    fun identError() {
        val errorField = arguments?.getString(getString(R.string.errorfield))
        val errorMessage = arguments?.getString(getString(R.string.errormessage))
        if (!errorField.isNullOrEmpty() && !errorMessage.isNullOrEmpty()) {
            when (errorField) {
                "email" -> ErrorUtils.setErrorText(binding.textInputLayoutEmail, errorMessage)
                "userName" -> ErrorUtils.setErrorText(binding.textInputLayoutUser, errorMessage)
                "phoneNumber" -> ErrorUtils.setErrorText(binding.textInputLayoutPhone, errorMessage)
            }
        }
    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun click() {
        binding.confirmationInfo.setOnClickListener {
            registerViewModel.apply {
                name.value = binding.textUserName.text.toString()
                email.value = binding.textUseremail.text.toString()
                phone.value = binding.textUserPhoneNumbre.text.toString()
            }

            if (registerViewModel.validateFields()) {
                findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
            } else {
                ErrorUtils.setErrorText(binding.textInputLayoutUser, getString(R.string.completa_campo))
                ErrorUtils.setErrorText(binding.textInputLayoutEmail,getString(R.string.completa_campo))
                ErrorUtils.setErrorText(binding.textInputLayoutPhone,getString(R.string.completa_campo))

                registerViewModel.validateFields()
            }
        }
    }

    private fun clearError() {
        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutUser)
        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutEmail)
        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutPhone)
    }


}


