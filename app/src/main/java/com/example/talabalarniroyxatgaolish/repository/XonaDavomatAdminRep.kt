package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.network.ApiService

class XonaDavomatAdminRep(val apiService: ApiService) {
    fun getStudentRoom(room_id: Long) = apiService.getRoomStudent(room_id)

    fun setDavomat(davomat: List<DavomatDataItem>) = apiService.setDavomat(davomat)
}