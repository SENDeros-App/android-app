package com.example.senderos4.network.socket

import android.util.Log
import com.example.senderos4.data.Markers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

object socketAlerts {

    private lateinit var socket:Socket

    fun initSocket(){
        val options = IO.Options()
        options.forceNew = true
        socket = IO.socket("http://192.168.1.35:5000")
    }

    fun connect(){
        socket.connect()
        Log.d("tag", "Connetion")
    }

    fun addMarkerListener(listener:Emitter.Listener){
        socket.on("Marcadores", listener)
        Log.d("tag", "Se recibio")
    }


    fun emitCreatedMarker(marker:Markers){
        val markerJSON = JSONObject()
        markerJSON.put("user",marker.user)
        markerJSON.put("latitud", marker.latitude)
        markerJSON.put("longitud", marker.longitude)
        markerJSON.put("type", marker.id)
        markerJSON.put("alerta", marker.type)

        val markerString = markerJSON.toString()

        socket.emit("Marcadores", markerString)
        Log.d("tag", "Se envio uno"+marker.type+marker.latitude)
    }
}