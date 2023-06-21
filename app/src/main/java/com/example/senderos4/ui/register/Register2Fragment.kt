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
import com.example.senderos4.databinding.FragmentRegister2Binding
import com.example.senderos4.ui.register.viewmodels.RegisterViewModel


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


    private fun setViewModel(){
        binding.viewmodelregister = registerViewModel
    }

    private fun handleUiStatus(status: RegisterUiStatus) {
        when (status) {
            is RegisterUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()

            }
            is RegisterUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is RegisterUiStatus.Success -> {
                registerViewModel.clearStatus()
                registerViewModel.clearData()
                findNavController().navigate(R.id.action_register2Fragment_to_loginFragment)
            }
            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        registerViewModel.clearData()

    }
}
