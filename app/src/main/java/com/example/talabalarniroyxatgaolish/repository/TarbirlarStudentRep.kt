package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class TarbirlarStudentRep(var apiService: ApiService) {
    fun getYigilishlar() = apiService.getYigilish()
    fun getRate() = apiService.getRate()
    fun getStudents() = apiService.getStudent()
}