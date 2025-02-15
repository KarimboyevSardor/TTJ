package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class YigilishlarAdminRep(var apiService: ApiService) {
    fun getYigilishlar() = apiService.getYigilish()
    fun getRate() = apiService.getRate()
    fun getStudents() = apiService.getStudent()
}