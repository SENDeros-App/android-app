package com.example.senderos4

import android.app.Application
import com.example.senderos4.data.users
import com.example.senderos4.ui.clasificacion.repositories.ClasificationRepository

class SenderosApplication:Application() {

    val clasificationRepository: ClasificationRepository by lazy {
        ClasificationRepository(users)
    }
}