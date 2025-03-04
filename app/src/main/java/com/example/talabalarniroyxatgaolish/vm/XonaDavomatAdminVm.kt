package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.XonaDavomatAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.davomatList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class XonaDavomatAdminVm : ViewModel(){
    private val TAG = "XONADAVOMATADMINVIEWMODEL"
    private val roomStudent = MutableStateFlow<Resource<MutableList<StudentDataItem>>>(Resource.Loading())
    val _roomStudent: MutableStateFlow<Resource<MutableList<StudentDataItem>>> get() = roomStudent
    private val roomDavomat = MutableStateFlow<Resource<MutableList<DavomatDataItem>>>(Resource.Loading())
    val _roomDavomat: MutableStateFlow<Resource<MutableList<DavomatDataItem>>> get() = roomDavomat
    fun getStudentRoom(context: Context, room_id: Long) {
        val xonaDavomatAdminRep = XonaDavomatAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        viewModelScope.launch {
            try {
                xonaDavomatAdminRep.getStudentRoom(room_id)
                    .catch {
                        roomStudent.emit(Resource.Error(it))
                    }
                    .collect {
                        roomStudent.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getStudentRoom: ${e.message}")
            }
        }
    }

    fun setDavomat(context: Context, davomat: List<DavomatDataItem>, activity: FragmentActivity) {
        val xonaDavomatAdminRep = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class]
        viewModelScope.launch {
            try {
                xonaDavomatAdminRep.setDavomat(davomat).enqueue(object : Callback<MutableList<DavomatDataItem>>{
                    override fun onResponse(
                        call: Call<MutableList<DavomatDataItem>>,
                        response: Response<MutableList<DavomatDataItem>>
                    ) {
                        if (response.isSuccessful) {
                            davomatList = response.body()!!
                            liveDates.davomatLiveData.value = davomatList
                            Log.d(TAG, "onResponse: ${response.body()!!.size}")
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<MutableList<DavomatDataItem>>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "setDavomat: ${e.message}")
            }
        }
    }

}