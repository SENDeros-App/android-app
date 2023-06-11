package com.example.senderos4.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.senderos4.R
import com.example.senderos4.ui.login.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText


class LoginFragment : Fragment() {

    private lateinit var userName: TextInputEditText
    private lateinit var userPassword:TextInputEditText
    private lateinit var buttonLogin:Button

    private val viewModel: LoginViewModel by viewModels{
        LoginViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        handleLogin()
    }

    private fun bind() {

        userName = requireView().findViewById(R.id.textUserName)
        userPassword = requireView().findViewById(R.id.textUserPassword)
        buttonLogin = requireView().findViewById(R.id.loginButton)
    }

    private fun handleLogin(){

        buttonLogin.setOnClickListener {

            val name = userName.text.toString().trim()
            val password = userPassword.text.toString().trim()
            val initiated = viewModel.loginUser(name, password)

            if(initiated){
                Toast.makeText(requireContext(), "exitoso",Toast.LENGTH_SHORT).show()
                userName.error  = null
                userPassword.error = null
            }else{
                Toast.makeText(requireContext(), "invalido",Toast.LENGTH_SHORT).show()
                userName.error  = "el nombre es invalido"
                userPassword.error = "la contraseña es invalida"
            }
        }

    }



}