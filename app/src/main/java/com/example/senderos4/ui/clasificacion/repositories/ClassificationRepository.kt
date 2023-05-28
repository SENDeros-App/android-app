package com.example.senderos4.ui.clasificacion.repositories

import com.example.senderos4.data.Header
import com.example.senderos4.data.User

    class ClassificationRepository(private val users:List<User>, private var headers:List<Header>) {

        fun getUsersTop():List<User>{
            val topUsers = users.sortedByDescending { it.px.toInt() }.take(17)
            return topUsers
        }
        fun getHeaders()=headers
    }