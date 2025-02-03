package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class LoginRep(val apiService: ApiService) {
    suspend fun auth(login: String, password: String) = apiService.auth(login, password)
}