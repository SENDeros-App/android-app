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

        val errorMessage = arguments?.getString("errorMessage")
        if (!errorMessage.isNullOrEmpty()) {
            binding.textInputLayoutUser.error = errorMessage
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
                setErrorText(binding.textInputLayoutUser, "Por favor, completa este campo")
                setErrorText(binding.textInputLayoutEmail, "Por favor, completa este campo")
                setErrorText(binding.textInputLayoutPhone, "Por favor, completa este campo")

                registerViewModel.validateFields()
            }
        }


    }


    private fun clearError() {
        binding.textInputLayoutUser.clearErrorOnFocusChange()
        binding.textInputLayoutEmail.clearErrorOnFocusChange()
        binding.textInputLayoutPhone.clearErrorOnFocusChange()
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

