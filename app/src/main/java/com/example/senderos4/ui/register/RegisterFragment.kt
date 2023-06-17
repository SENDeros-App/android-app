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
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        click()

    }

    private fun click() {

        binding.confirmationInfo.setOnClickListener {
            if (validateCampos()) {
                val name = binding.textUserName.text.toString()
                val email = binding.textUseremail.text.toString()
                val phone = binding.textUserPhoneNumbre.text.toString()

                registerViewModel.name.value = name
                registerViewModel.email.value = email
                registerViewModel.phone.value = phone

                findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun validateCampos(): Boolean {
        val name = binding.textUserName.text.toString()
        val email = binding.textUseremail.text.toString()
        val phone = binding.textUserPhoneNumbre.text.toString()

        // Validar que todos los campos estén completos
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return false
        }

        // Validar el número de teléfono
        if (!validatePhoneNumber(phone)) {
            Toast.makeText(requireContext(), "El número de teléfono ingresado no es válido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^\\d{8}$")
        return regex.matches(phoneNumber)
    }



}
