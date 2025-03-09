package com.example.talabalarniroyxatgaolish.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

class SocketManager {

    private var socket: Socket? = null
    private val TAG = "SOCKET_MANAGER"

    init {
        try {
            socket = IO.socket("http://192.168.246.124:3000") // ✅ IP-manzilingizni tekshiring
            socket?.connect()
            Log.d(TAG, "✅ Socket serverga ulandi")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Socket ulanishida xatolik: ${e.message}")
        }

        // 🔔 Bildirishnomani olish
        onNotification()
    }

    fun registerUser(userId: String) {
        if (socket?.connected() == true) {
            socket?.emit("registerUser", userId)
            Log.d(TAG, "✅ registerUser event jo‘natildi: $userId")
        } else {
            Log.e(TAG, "❌ Socket hali ulanmagan, registerUser jo‘natilmadi!")
        }
    }

    private fun onNotification() {
        socket?.on("notification") { args ->
            if (args.isNotEmpty()) {
                try {
                    val data = args[0] as JSONObject
                    val title = data.getString("title")
                    val message = data.getString("message")
                    Log.d(TAG, "🔔 Bildirishnoma keldi: $title - $message")
                } catch (e: Exception) {
                    Log.e(TAG, "❌ JSON o‘qishda xatolik: ${e.message}")
                }
            }
        }
    }

    fun disconnect() {
        socket?.disconnect()
        Log.d(TAG, "❌ Socket uzildi")
    }
}
