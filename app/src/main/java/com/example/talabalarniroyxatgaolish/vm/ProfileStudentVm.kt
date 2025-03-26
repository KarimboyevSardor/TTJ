package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.StudentInfo
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.utils.Utils.studentInfoData
import kotlinx.coroutines.launch

class ProfileStudentVm : ViewModel() {
    private val TAG = "PROFILESTUDENTVIEWMODEL"
    private val studentInfo = SingleLiveEvent<String>()
    private val student = SingleLiveEvent<StudentInfo>()
    val _studentInfo get() = studentInfo
    val _student get() = student

    fun getStudentInfo(context: Context, id: Long) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.getStudentInfo(id)
                when(response.code()) {
                    404 -> {
                        studentInfo.postValue("Ma'lumot topilmadi.")
                    }
                    500 -> {
                        studentInfo.postValue("Server bilan ulanib bo'lmadi.")
                    }
                    200 -> {
                        studentInfo.postValue("qoshildi")
                        studentInfoData = response.body()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "getStudentInfo: ${e.message}")
            }
        }
    }
}