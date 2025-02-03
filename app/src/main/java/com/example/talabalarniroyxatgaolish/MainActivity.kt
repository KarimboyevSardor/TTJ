package com.example.talabalarniroyxatgaolish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.talabalarniroyxatgaolish.databinding.ActivityMainBinding
import com.example.talabalarniroyxatgaolish.ui.Login

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) { // faqat birinchi marta yuklash uchun
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Login()) // containerga Fragment joylash
                .commit()
        }
    }
}