package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class AdminRep(val apiService: ApiService) {
    fun getAdmin() = apiService.getAdmin()

    fun getAdmin(id: Long) = apiService.deleteAdmin(id = id)
}