package com.example.senderos4

import android.app.Application
import com.example.senderos4.data.headers
import com.example.senderos4.data.users
import com.example.senderos4.ui.clasificacion.repositories.ClassificationRepository
import com.example.senderos4.ui.login.repositories.LoginRepository

class SenderosApplication:Application() {

    val classificationRepository: ClassificationRepository by lazy {
        ClassificationRepository(users, headers)
    }

    val loginRepository:LoginRepository by lazy {
        LoginRepository(users)
    }
}