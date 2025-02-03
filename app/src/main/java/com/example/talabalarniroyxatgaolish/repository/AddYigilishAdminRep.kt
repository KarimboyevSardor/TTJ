package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddYigilishAdminRep(val apiService: ApiService) {
    fun addYigilish(
        image: MultipartBody.Part?,
        name: RequestBody,
        time: RequestBody,
        description: RequestBody,
        meetingPlace: RequestBody
        )
    = apiService.addYigilish(
        image, name, time, description, meetingPlace
    )

    fun addRate(addRep: AddRateReq) = apiService.addRate(addRep.addRateReq)
}