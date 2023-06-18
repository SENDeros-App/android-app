package com.example.senderos4.ui.register

import android.os.Bundle
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
    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun click() {
        binding.confirmationInfo.setOnClickListener {
            if (validateCampos()) {
                registerViewModel.apply {
                    name.value = binding.textUserName.text.toString()
                    email.value = binding.textUseremail.text.toString()
                    phone.value = binding.textUserPhoneNumbre.text.toString()
                }

                findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
            }
        }

        binding.initSesion.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validateCampos(): Boolean {
        with(binding) {
            val name = textUserName.text.toString()
            val email = textUseremail.text.toString()
            val phone = textUserPhoneNumbre.text.toString()

            // Validar que todos los campos estén completos
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los correctamente", Toast.LENGTH_SHORT).show()
                return false
            }

            // Validar el número de teléfono
            if (!validatePhoneNumber(phone)) {
                Toast.makeText(requireContext(), "El número de teléfono ingresado no es válido", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^\\d{8}$")
        return regex.matches(phoneNumber)
    }
}

