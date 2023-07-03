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
import com.example.senderos4.databinding.FragmentRegister2Binding
import com.example.senderos4.ui.register.viewmodels.RegisterViewModel
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
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun observeStatus() {
        registerViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }

        registerViewModel.errorUser.observe(viewLifecycleOwner){
            binding.textInputLayoutUser.error = it
        }

        registerViewModel.errorPassword.observe(viewLifecycleOwner){
            binding.textInputLayoutPassword.error = it
            binding.textInputLayoutPassword2.error = it
        }

        registerViewModel.errorPasswordConfirmation.observe(viewLifecycleOwner){
            binding.textInputLayoutPassword2.error = it
            binding.textInputLayoutPassword2.error = it
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
                //registerViewModel.clearStatus()
                //Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
                when (status.message) {
                    getString(R.string.email_ya_registrado) -> {
                        findNavController().navigate(R.id.action_register2Fragment_to_registerFragment)
                    }
                    /*getString(R.string.usuario_ya_registrado) -> {

                        ErrorUtils.setErrorText(binding.textInputLayoutUser, getString(R.string.usuario_ya_registrado))
                    }*/
                    getString(R.string.telefonico_ya_registrado) ->{
                        findNavController().navigate(R.id.action_register2Fragment_to_registerFragment)
                    }
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


