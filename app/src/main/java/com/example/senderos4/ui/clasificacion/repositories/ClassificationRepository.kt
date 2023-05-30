package com.example.senderos4.ui.clasificacion.repositories

import com.example.senderos4.data.Header
import com.example.senderos4.data.User

class ClassificationRepository(private val users:List<User>, private var headers:List<Header>) {
    fun getUsersTop():List<User>{
         val topUsers = users.sortedByDescending { it.px.toInt() }.take(17)

        //prueba para mandar la numeracino segun posicion
        for ((i, v) in topUsers.withIndex()){
            println("[${i+1}, $v]")
        }

        return topUsers
    }
    fun getHeaders()=headers

}