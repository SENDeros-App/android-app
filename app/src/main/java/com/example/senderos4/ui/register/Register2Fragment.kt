package com.example.senderos4.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.senderos4.R
import com.example.senderos4.databinding.FragmentRegister2Binding


class Register2Fragment : Fragment() {

    private lateinit var binding:FragmentRegister2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegister2Binding.inflate(inflater, container, false)
        return binding.root
    }


}