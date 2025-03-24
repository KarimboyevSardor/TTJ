package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class LoginRep(val apiService: ApiService) {
    fun auth(login: String, password: String, device: String, date: String) = apiService.auth(login, password, device, date)
}