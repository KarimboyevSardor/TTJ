package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.XonalarAdminRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class XonalarAdminVm : ViewModel() {
    private val stateXona = MutableStateFlow<Resource<MutableList<XonaDataItem>>>(Resource.Loading())
    val _stateXona: MutableStateFlow<Resource<MutableList<XonaDataItem>>> get() = stateXona
    fun getXona(context: Context) {
        viewModelScope.launch {
            try {
                val xonalarAdminRep = XonalarAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
                xonalarAdminRep.getXona()
                    .catch {
                        stateXona.emit(Resource.Error(it))
                    }
                    .collect{
                        stateXona.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}