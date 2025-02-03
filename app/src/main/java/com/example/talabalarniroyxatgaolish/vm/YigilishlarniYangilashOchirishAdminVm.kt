package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddEditYigilish
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.YigilishlarYangilashOchirishAdminRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class YigilishlarniYangilashOchirishAdminVm : ViewModel() {
    private val stateYigilish = MutableStateFlow<Resource<AddEditYigilish>>(Resource.Loading())
    val _stateYigilish: MutableStateFlow<Resource<AddEditYigilish>> get() = stateYigilish
    private val stateDeleteYigilish = MutableStateFlow<Resource<Message>>(Resource.Loading())
    val _stateDeleteYigilish: MutableStateFlow<Resource<Message>> get() = stateDeleteYigilish
    fun editYigilish(id: Long, time: RequestBody, description: RequestBody, name: RequestBody, meeting_place: RequestBody, image: MultipartBody.Part?, context: Context) {
        val editYigilishRep = YigilishlarYangilashOchirishAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                editYigilishRep.editYigilish(id = id, image = image, name = name, time = time, description = description, meeting_place = meeting_place)
                    .catch {
                        stateYigilish.emit(Resource.Error(it))
                    }
                    .collect {
                        stateYigilish.emit(Resource.Success(it))
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteYigilish(id: Long, context: Context) {
        val editYigilishRep = YigilishlarYangilashOchirishAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                editYigilishRep.deleteYigilish(id = id)
                    .catch {
                        stateDeleteYigilish.emit(Resource.Error(it))
                    }
                    .collect{
                        stateDeleteYigilish.emit(Resource.Success(it))
                    }
            }
        } catch (e: Exception) {
            throw  e
        }
    }
}