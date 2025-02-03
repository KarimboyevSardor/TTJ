package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.DavomatAdminRep
import com.example.talabalarniroyxatgaolish.repository.XonalarAdminRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DavomatAdminVm : ViewModel() {
    private val TAG = "DAVOMATADMINVIEWMODEL"
    private val xona = MutableStateFlow<Resource<MutableList<XonaDataItem>>>(Resource.Loading())
    val _xona: MutableStateFlow<Resource<MutableList<XonaDataItem>>> get() = xona
    private val dateDavomat = MutableStateFlow<Resource<MutableList<DavomatDataItem>>>(Resource.Loading())
    val _dateDavomat: MutableStateFlow<Resource<MutableList<DavomatDataItem>>> get() = dateDavomat
    fun getXona(context: Context) {
        viewModelScope.launch {
            try {
                val xonalarAdminRep = XonalarAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
                xonalarAdminRep.getXona()
                    .catch {
                        xona.emit(Resource.Error(it))
                    }
                    .collect{
                        xona.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getDateDavomat(context: Context, date: String) {
        viewModelScope.launch {
            try {
                val davomatAdminRep = DavomatAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
                davomatAdminRep.getDateDavomat(date)
                    .catch {
                        dateDavomat.emit(Resource.Error(it))
                    }
                    .collect{
                        dateDavomat.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getDavomat: ${e.message}")
            }
        }
    }
}