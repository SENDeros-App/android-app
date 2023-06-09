package com.example.senderos4.ui.alerts.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.senderos4.databinding.FragmentAlertMenuBinding
import com.example.senderos4.ui.alerts.recyclerview.alertsRecyclerViewAdapter
import com.example.senderos4.ui.alerts.viewmodel.alertViewModel

class alertMenuFragment : Fragment() {
    private lateinit var binding: FragmentAlertMenuBinding
    private lateinit var adapter: alertsRecyclerViewAdapter
    private lateinit var viewModel: alertViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = alertsRecyclerViewAdapter()
        binding.recyclerViewAlert.adapter = adapter
        binding.recyclerViewAlert.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(alertViewModel::class.java)
        viewModel.getAlertas().observe(viewLifecycleOwner) { alertas ->
            adapter.setAlertas(alertas)
        }

        return view
    }
}