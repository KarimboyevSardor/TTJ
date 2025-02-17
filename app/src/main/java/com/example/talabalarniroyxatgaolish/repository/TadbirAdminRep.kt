package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService

class TadbirAdminRep(val apiService: ApiService) {
    fun getRateMeetingId(meeting_id: Long) = apiService.getRateMeetingId(meeting_id)
}