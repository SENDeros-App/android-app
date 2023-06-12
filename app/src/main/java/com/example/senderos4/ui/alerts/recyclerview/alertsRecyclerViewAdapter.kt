package com.example.senderos4.ui.alerts.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.ItemAlertaBinding
import com.example.senderos4.ui.alerts.model.Alerta

class alertsRecyclerViewAdapter : RecyclerView.Adapter<alertsRecyclerViewAdapter.AlertViewHolder>() {

    private var alertas: List<Alerta> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = .inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alerta = alertas[position]
        holder.bind(alerta)
    }

    override fun getItemCount(): Int {
        return alertas.size
    }

    fun setAlertas(alertas: List<Alerta>) {
        this.alertas = alertas
        notifyDataSetChanged()
    }

    inner class AlertViewHolder(private val binding: ItemAlertaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alerta: Alerta) {
            binding.alerta = alerta
            binding.executePendingBindings()
        }
    }
}