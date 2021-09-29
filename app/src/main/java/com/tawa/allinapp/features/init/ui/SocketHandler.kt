package com.tawa.allinapp.features.init.ui

import android.util.Log
import com.tawa.allinapp.data.local.Prefs
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import javax.inject.Inject

class SocketHandler @Inject constructor(private val prefs: Prefs) {

    lateinit var socket: Socket

    @Synchronized
    fun setSock() {
        try {
            val options = IO.Options()
            val headers = mutableMapOf<String, List<String>>()
            headers["id_user"] = listOf(prefs.idUser ?: "")
            options.extraHeaders = headers
            socket =
                IO.socket("http://alb-all-in-web-1372832709.us-east-1.elb.amazonaws.com", options)
        } catch (e: URISyntaxException) {
            Log.d("erroSocket", e.toString())
        }

    }


    @Synchronized
    fun getSock(): Socket {
        return socket
    }

}