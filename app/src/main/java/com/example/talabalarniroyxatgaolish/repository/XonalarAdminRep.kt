package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiService

class XonalarAdminRep(val apiService: ApiService) {
    fun getXona() = apiService.getXona()
    fun addXona(xona: XonaDataItem) = apiService.addXona(xona)
}