package com.example.senderos4.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.example.senderos4.SenderosApplication
import com.example.senderos4.databinding.FragmentLoginBinding
import com.example.senderos4.ui.login.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private  val loginViewModel: LoginViewModel by  activityViewModels {
        LoginViewModel.Factory
    }

    val app by lazy {
        requireActivity().application as SenderosApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val token = app.getTokent() // Obtener el token

        if (token.isNotEmpty()) {
            // El usuario ya ha iniciado sesión antes, va a la pantalla principal
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            return
        }*/

        setViewModel()
        observeStatus()

    }

    private fun setViewModel() {
        binding.viewmodel = loginViewModel
    }


    private fun observeStatus() {
        loginViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }


    private fun handleUiStatus(status: LoginUiStatus) {
        when(status) {
            is LoginUiStatus.Error -> {
                Toast.makeText(requireContext(), "Error has occurred", Toast.LENGTH_SHORT).show()
            }
            is LoginUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is LoginUiStatus.Success -> {
                loginViewModel.clearStatus()
                loginViewModel.clearData()
                app.saveAuthToken(status.token)
                Toast.makeText(requireContext(), "Logueado", Toast.LENGTH_LONG).show()
            }

            else -> {}
        }
    }

}