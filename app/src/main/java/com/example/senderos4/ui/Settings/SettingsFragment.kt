package com.example.senderos4.ui.Settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.R
import com.example.senderos4.SenderosApplication


class SettingsFragment : HiddenMenuFragment() {

    val app by lazy {
        requireActivity().application as SenderosApplication
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

}