package com.example.senderos4.ui.clasificacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.senderos4.hiddenMenu.HiddenMenuFragment
import com.example.senderos4.R
import com.example.senderos4.ui.clasificacion.adapter.ClasificationAdapter
import com.example.senderos4.ui.clasificacion.viewmodels.ClasificationViewModel

class ClasificationsFragment : HiddenMenuFragment() {

    private  val viewModel: ClasificationViewModel by viewModels{
        ClasificationViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<RecyclerView>(R.id.recyclerView).adapter = ClasificationAdapter().apply {
            submitData(viewModel.getHeaders(), viewModel.getUsers())
        }
    }

}