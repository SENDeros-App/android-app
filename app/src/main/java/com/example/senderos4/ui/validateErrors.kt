package com.example.senderos4.ui.register

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.google.android.material.textfield.TextInputLayout

object ErrorUtils {

    fun clearErrorOnFocusChange(textInputLayout: TextInputLayout) {
        textInputLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.error = null
            }
        }
    }

    fun setErrorText(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.error = errorMessage
        textInputLayout.clearFocus()
    }

    fun navigateToRegisterFragment(fragment: Fragment, errorField: String, errorMessage: String) {
        val bundle = bundleOf("errorField" to errorField, "errorMessage" to errorMessage)
        fragment.findNavController().navigate(R.id.action_register2Fragment_to_registerFragment, bundle)
    }
}
