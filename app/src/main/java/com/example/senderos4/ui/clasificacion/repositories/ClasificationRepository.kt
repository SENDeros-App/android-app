package com.example.senderos4.ui.clasificacion.repositories

import com.example.senderos4.data.User

class ClasificationRepository(private val users:List<User>) {

    fun getUsuarios()=users
}