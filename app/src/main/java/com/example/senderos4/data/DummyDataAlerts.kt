package com.example.senderos4.data

import com.example.senderos4.R
import com.example.senderos4.ui.alerts.model.Alerta

object DummyDataAlerts {
    fun getAlerts(): List<Alerta> {
        return listOf(
            Alerta("Usuario 1", "Alerta 1", R.drawable.setting_icon),
            Alerta("Usuario 2", "Alerta 2", R.drawable.setting_icon),
            Alerta("Usuario 3", "Alerta 3", R.drawable.setting_icon)
        )
    }
}