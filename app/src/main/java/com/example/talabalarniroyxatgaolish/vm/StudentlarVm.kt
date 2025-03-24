package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.XonalarAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StudentlarVm : ViewModel() {
    private val TAG = "STUDENTLARVIEWMODEL"
    private val getStudentlar = SingleLiveEvent<String>()
    val _getStudentlar get() = getStudentlar
    private val deleteStduent = SingleLiveEvent<String>()
    val _deleteStudent get() = deleteStduent
    private val stateXona = MutableStateFlow<Resource<MutableList<XonaDataItem>>>(Resource.Loading())
    val _stateXona: MutableStateFlow<Resource<MutableList<XonaDataItem>>> get() = stateXona
    fun getStudents(context: Context) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.getStudents()
                when(response.code()) {
                    404 -> getStudentlar.postValue("Studentlar mavjud emas.")
                    200 -> {
                        getStudentlar.postValue("topildi")
                        studentlarList = response.body()!!
                    }
                    500 -> getStudentlar.postValue("Server bilan ulanib bo'lmadi qaytadan uruning.")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getStudents: ${e.message}")
            }
        }
    }

    fun deleteStudent(context: Context, id: Long) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.deleteStudent(id)
                when(response.code()) {
                    404 -> deleteStduent.postValue("Malumot topilmadi.")
                    200 -> {
                        deleteStduent.postValue("O'chirildi.")
                        studentlarList.remove(studentlarList.filter { it.id == id }[0])
                        Log.d(TAG, "deleteStudent: $studentlarList")
                    }
                    500 -> deleteStduent.postValue("Server bilan ulanib bo'lmadi qaytadan uruning.")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getStudents: ${e.message}")
            }
        }
    }

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