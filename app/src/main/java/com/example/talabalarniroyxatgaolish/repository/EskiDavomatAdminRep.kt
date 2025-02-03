package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class EskiDavomatAdminRep(val apiService: ApiService) {
    fun getDavomat() = apiService.getDavomat()
}