package com.example.senderos4.ui.alerts.repository

import com.example.senderos4.ui.alerts.model.Alerta
import com.example.senderos4.data.DummyDataAlerts

class alertRepository {
    fun getAlertas(): List<Alerta> {
        return DummyDataAlerts.getAlerts()
    }
}