package com.example.senderos4.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.senderos4.R
import com.example.senderos4.databinding.FragmentHistoryBinding
import com.example.senderos4.databinding.FragmentLoginBinding

class HistoryFragment : Fragment() {

    private lateinit var binding:FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}