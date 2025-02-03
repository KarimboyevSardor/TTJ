package com.example.talabalarniroyxatgaolish.repository

import com.example.talabalarniroyxatgaolish.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class YigilishlarYangilashOchirishAdminRep(val apiService: ApiService) {
    fun editYigilish(
        id: Long,
        image: MultipartBody.Part?,
        name: RequestBody,
        time: RequestBody,
        description: RequestBody,
        meeting_place: RequestBody
    ) = apiService.editYigilish(
        id = id,
        image = image,
        name = name,
        time = time,
        description = description,
        meeting_place = meeting_place
    )

    fun deleteYigilish(id: Long) = apiService.deleteYigilish(id = id)
}