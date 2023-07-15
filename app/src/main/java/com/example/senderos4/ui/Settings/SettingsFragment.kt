package com.example.senderos4.ui.Settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.senderos4.R
import com.example.senderos4.SenderosApplication
import com.example.senderos4.databinding.FragmentLoginBinding
import com.example.senderos4.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding:FragmentSettingsBinding

    val app by lazy {
        requireActivity().application as SenderosApplication
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

}