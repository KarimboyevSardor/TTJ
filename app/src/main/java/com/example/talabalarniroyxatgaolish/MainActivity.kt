package com.example.talabalarniroyxatgaolish

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.talabalarniroyxatgaolish.databinding.ActivityMainBinding
import com.example.talabalarniroyxatgaolish.network.SocketManager
import com.example.talabalarniroyxatgaolish.ui.Login
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var socket: Socket
    private val TAG = "MAINACTIVITY"
    lateinit var socketManager: SocketManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) { // faqat birinchi marta yuklash uchun
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Login()) // containerga Fragment joylash
                .commit()
        }

        socketManager = SocketManager()
        socketManager.registerUser("user_12345")
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}