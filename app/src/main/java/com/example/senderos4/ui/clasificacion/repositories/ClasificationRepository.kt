package com.example.senderos4.ui.clasificacion.repositories

import com.example.senderos4.data.Header
import com.example.senderos4.data.User

class ClasificationRepository(private val users:List<User>, private var headers:List<Header>) {

    fun getUsersTop() =users
    fun getHeaders()=headers
}