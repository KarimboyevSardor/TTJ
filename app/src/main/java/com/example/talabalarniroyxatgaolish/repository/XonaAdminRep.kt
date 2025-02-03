package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiService

class XonaAdminRep(val apiService: ApiService) {
    fun editXona(xona: XonaDataItem) = apiService.editXona(xona)
    fun deleteXona(id: Long) = apiService.deleteXona(id)
    fun getStudentRoomId() = apiService.getStudentRoomId()
    fun getAuthId(id: Long) = apiService.getLoginId(id)
}