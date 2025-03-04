package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.network.ApiService

class EditAdminRep(val apiService: ApiService) {
    fun getAuth(id: Long) = apiService.getAuth(id)
}