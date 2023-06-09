package com.example.senderos4.ui.alerts.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.senderos4.ui.alerts.model.Alerta
import com.example.senderos4.ui.alerts.repository.alertRepository

class alertViewModel : ViewModel() {
    private val repository: alertRepository = alertRepository()
    private val _alertas: MutableLiveData<List<Alerta>> = MutableLiveData()

    init {
        fetchAlertas()
    }

    fun getAlertas(): LiveData<List<Alerta>> {
        return _alertas
    }

    private fun fetchAlertas() {
        val alertas = repository.getAlertas()
        _alertas.value = alertas
    }
}