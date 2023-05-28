package com.example.senderos4

import android.app.Application
import com.example.senderos4.data.headers
import com.example.senderos4.data.users
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository

class SenderosApplication:Application() {

    val classificationRepository: ClassificationRepository by lazy {
        ClassificationRepository(users, headers)
    }
}