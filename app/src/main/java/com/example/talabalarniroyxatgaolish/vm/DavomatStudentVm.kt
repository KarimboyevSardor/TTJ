package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.DavomatAdminRep
import com.example.talabalarniroyxatgaolish.repository.DavomatStudentRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DavomatStudentVm : ViewModel() {
    private val TAG = "DAVOMATSTUDENTVIEWMODEL"
    private val davomat = MutableStateFlow<Resource<MutableList<DavomatDataItem>>>(Resource.Loading())
    val _davomat: MutableStateFlow<Resource<MutableList<DavomatDataItem>>> get() = davomat
    fun getDavomat(context: Context) {
        viewModelScope.launch {
            try {
                val davomatAdminRep = DavomatStudentRep(
                    ApiClient.getRetrofit(context).create(
                        ApiService::class.java))
                davomatAdminRep.getDavomat()
                    .catch {
                        davomat.emit(Resource.Error(it))
                    }
                    .collect{
                        davomat.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getDavomat: ${e.message}")
            }
        }
    }
}