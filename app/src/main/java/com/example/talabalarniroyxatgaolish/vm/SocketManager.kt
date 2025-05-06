package com.example.talabalarniroyxatgaolish.vm

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URI

class SocketManager {
    private var socket: Socket? = null

    init {
        try {
            // Serverning URI manzili (serveringizning IP yoki URL manzili)
            val uri = URI("https://ttj-api-production.up.railway.app/")

            // Socket.IO serveriga ulanish
            socket = IO.socket(uri)

            // Ulanish va xabarlarni olish
            socket?.on(Socket.EVENT_CONNECT) {
                println("Serverga ulanish muvaffaqiyatli")
            }

            // 'meetingReminder' hodisasini tinglash
            socket?.on("meetingReminder", Emitter.Listener { args ->
                if (args.isNotEmpty()) {
                    val message = args[0] as String
                    // Bildirishnomani olish va foydalanuvchiga ko'rsatish
                    println("Bildirishnoma: $message")
                }
            })

            // Socket.IO serveriga ulanish
            socket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Serverga ulanishni yopish
    fun disconnect() {
        socket?.disconnect()
    }
}