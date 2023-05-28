package com.example.senderos4.ui.clasificacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.senderos4.SenderosApplication
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository

class ClassificationViewModel(private val userRepository: ClassificationRepository):ViewModel() {

    fun getTop()= userRepository.getUsersTop()
    fun getHeaders()=userRepository.getHeaders()
    companion object{
        val Factory = viewModelFactory {
            initializer {
                val userRepository = (this[APPLICATION_KEY] as SenderosApplication).classificationRepository
                ClassificationViewModel(userRepository)
            }
        }
    }

}