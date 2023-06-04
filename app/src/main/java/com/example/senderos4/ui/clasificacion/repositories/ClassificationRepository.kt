package com.example.senderos4.ui.clasificacion.repositories

import com.example.senderos4.data.Header
import com.example.senderos4.data.User

class ClassificationRepository(private val users:List<User>, private var headers:List<Header>) {
    /*fun getUsersTop():List<User>{
         val topUsers = users.sortedByDescending { it.px.toInt() }.take(17)

        //prueba para mandar la numeracino segun posicion
        /*for ((i, v) in topUsers.withIndex()){
            println("[${i+1}, $v]")
        }*/
        return topUsers
    }*/


    private fun divideUsersByDivision(): Map<String, List<User>> {
        return users.groupBy { it.division }
    }
    fun getUsersTopByDivision(division: String, count: Int): List<User> {
        val usersForDivision = usersByDivision[division] ?: emptyList()
        return usersForDivision
            .sortedByDescending { it.px.toInt() }
            .take(count)
    }

    private val usersByDivision: Map<String, List<User>> = divideUsersByDivision()
    fun getHeaders()=headers
}