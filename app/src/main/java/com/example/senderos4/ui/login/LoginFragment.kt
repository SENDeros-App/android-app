package com.example.senderos4.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.senderos4.MainActivity
import com.example.senderos4.R
import com.example.senderos4.SenderosApplication
import com.example.senderos4.databinding.FragmentLoginBinding
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.ui.login.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import retrofit2.HttpException


class LoginFragment : HiddenMenuFragment() {

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

                if (status.exception is HttpException){
                    when(status.exception.code()) {
                        404 ->
                        Toast.makeText(requireContext(), "Malas credenciales", Toast.LENGTH_SHORT).show()
                        500 ->
                            Toast.makeText(requireContext(), "Error al conectarse al servidor ...", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(requireContext(), "Error has occurred", Toast.LENGTH_SHORT).show()
                }
                Log.d("Error Login", "error", status.exception)
            }
            is LoginUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is LoginUiStatus.Success -> {
                loginViewModel.clearStatus()
                loginViewModel.clearData()
                app.saveAuthToken(status.token)
                findNavController().navigate(R.id.action_loginFragment_to_map_fragment)
            }
            else -> {}
        }
    }

}