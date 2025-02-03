package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.YigilishlarDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.YigilishlarAdminRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class YigilishlarAdminVm : ViewModel(){
    private val stateYigilishlar = MutableStateFlow<Resource<MutableList<YigilishlarDataItem>>>(Resource.Loading())
    val _stateYigilishlar: MutableStateFlow<Resource<MutableList<YigilishlarDataItem>>> get() = stateYigilishlar
    private val rates = MutableStateFlow<Resource<MutableList<Rate>>>(Resource.Loading())
    val _rates: MutableStateFlow<Resource<MutableList<Rate>>> get() = rates
    fun getYigilishlar(context: Context) {
        val yigilishRep = YigilishlarAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                yigilishRep.getYigilishlar().catch {
                    stateYigilishlar.emit(Resource.Error(it))
                }.collect{
                    stateYigilishlar.emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun getRates(context: Context) {
        val yigilishRep = YigilishlarAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                yigilishRep.getRate()
                    .catch {
                        rates.emit(Resource.Error(it))
                    }
                    .collect{
                        rates.emit(Resource.Success(it))
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}