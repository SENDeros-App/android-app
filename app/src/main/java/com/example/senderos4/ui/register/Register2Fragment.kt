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
        //clearError()
    }

    private fun observeStatus() {
        registerViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }

        registerViewModel.passwordMatch.observe(viewLifecycleOwner) { passwordMatch ->
            handlePasswordMatch(passwordMatch)
        }

        registerViewModel.userError.observe(viewLifecycleOwner) { error ->
            handleUserError(error)
        }

        registerViewModel.passwordError.observe(viewLifecycleOwner) { error ->
            handlePasswordError(error)
        }

        registerViewModel.userError.observe(viewLifecycleOwner) { error ->
            handleUserError(error)
        }
    }

    private fun handleUserError(error: String?) {
        binding.textInputLayoutUser.error = error
    }

    private fun handlePasswordError(error: String?) {
        binding.textInputLayoutPassword.error = error
        binding.textInputLayoutPassword2.error = error
        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutPassword)
        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutPassword2)

    }

    private fun handlePasswordMatch(passwordMatch: Boolean) {
        val passwordTextInputLayouts = listOf(
            binding.textInputLayoutPassword,
            binding.textInputLayoutPassword2
        )

        if (!passwordMatch) {
            passwordTextInputLayouts.forEach { textInputLayout ->
                textInputLayout.error = getString(R.string.contraseno_coinciden)
                ErrorUtils.clearErrorOnFocusChange(textInputLayout)
            }
        } else {
            passwordTextInputLayouts.forEach { textInputLayout ->
                textInputLayout.error = null
            }
        }
    }


    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun handleUiStatus(status: RegisterUiStatus) {
        when (status) {
            is RegisterUiStatus.Error -> {
                if (status.exception is HttpException) {
                    when (status.exception.code()) {
                        500 -> Toast.makeText(
                            requireContext(),
                            getString(R.string.error_conectarse_servidor),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_has_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("Error Login", "error", status.exception)
            }

            is RegisterUiStatus.ErrorWithMessage -> {
                registerViewModel.clearStatus()
                //Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
                when (status.message) {
                    getString(R.string.email_ya_registrado) -> ErrorUtils.navigateToRegisterFragment(
                        this,
                        "email",
                        getString(R.string.email_ya_registrado)
                    )
                    getString(R.string.usuario_ya_registrado) -> {
                        ErrorUtils.setErrorText(binding.textInputLayoutUser, getString(R.string.usuario_ya_registrado))
                        ErrorUtils.clearErrorOnFocusChange(binding.textInputLayoutUser)
                    }
                    getString(R.string.telefonico_ya_registrado) -> ErrorUtils.navigateToRegisterFragment(
                        this,
                        "phoneNumber",
                        getString(R.string.telefonico_ya_registrado)
                    )
                }
            }

            is RegisterUiStatus.Success -> {
                registerViewModel.clearStatus()
                registerViewModel.clearData()
                findNavController().navigate(R.id.action_register2Fragment_to_loginFragment)
            }

            else -> {
            }
        }
    }
}


