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
        // Inflate the layout for this fragment
        binding = FragmentRegister2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeStatus()
        setViewModel()
    }



    private fun observeStatus() {
        registerViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
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
                    "email ya registrado ", "Usuario ya registrado ", "telefonico ya registrado" -> {
                        findNavController().navigate(R.id.action_register2Fragment_to_registerFragment)
                    }
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

    /*override fun onPause() {
        super.onPause()
        registerViewModel.clearData()

    }*/
}
