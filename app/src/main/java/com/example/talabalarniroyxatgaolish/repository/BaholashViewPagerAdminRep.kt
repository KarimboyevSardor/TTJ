package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.network.ApiService

class BaholashViewPagerAdminRep(val apiService: ApiService) {
    fun editRate(editRate: List<Rate>) = apiService.editRate(editRate)
}