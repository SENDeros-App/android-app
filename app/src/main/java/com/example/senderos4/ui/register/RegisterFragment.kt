package com.example.senderos4.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.example.senderos4.databinding.FragmentRegisterBinding


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
        identError()
        clicknav()
        binding.lifecycleOwner = viewLifecycleOwner

    }

    private fun clicknav() {
        binding.initSesion.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    fun identError() {
        registerViewModel.errorName.observe(viewLifecycleOwner) {
            binding.textInputLayoutUser.error = it
        }

        registerViewModel.errorEmail.observe(viewLifecycleOwner){
            binding.textInputLayoutEmail.error = it
        }

        registerViewModel.errorPhoneNumber.observe(viewLifecycleOwner){
            binding.textInputLayoutPhone.error = it
        }
    }

    private fun setViewModel() {
        binding.viewmodelregister = registerViewModel
    }

    private fun click() {
        binding.confirmationInfo.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_register2Fragment)
        }
    }


}


