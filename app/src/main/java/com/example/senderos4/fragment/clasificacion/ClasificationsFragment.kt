package com.example.senderos4.fragment.clasificacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.senderos4.fragment.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.R

class ClasificationsFragment : HiddenMenuFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasifications, container, false)
    }


}