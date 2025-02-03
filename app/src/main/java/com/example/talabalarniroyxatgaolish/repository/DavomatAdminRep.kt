package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class DavomatAdminRep(val apiService: ApiService) {
    fun getDavomat() = apiService.getDavomat()
    fun getDateDavomat(date: String) = apiService.getDateDavomat(date)
}