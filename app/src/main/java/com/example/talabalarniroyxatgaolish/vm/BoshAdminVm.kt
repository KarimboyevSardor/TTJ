package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import kotlinx.coroutines.launch

class BoshAdminVm : ViewModel() {
    private val TAG = "BOSHADMINVIEWMODEL"
    private val admin =  SingleLiveEvent<String>()
    val _admin get() = admin

    fun getAdminId(context: Context, id: Long) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.getAdminId(id)
                when(response.code()) {
                    200 -> {
                        admin.postValue(response.body()!!.image_url)
                    }
                    500 -> {
                        admin.postValue("Serverga ulanib bo'lmadi.")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "getAdminId: ${e.message}")
            }
        }
    }
}