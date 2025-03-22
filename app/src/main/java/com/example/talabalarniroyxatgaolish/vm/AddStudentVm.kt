package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddedStudentDataItem
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStudentVm : ViewModel() {
    private val TAG = "ADDSTUDENTVIEWMODEL"
    private val _uiState = SingleLiveEvent<String>()
    var uiState: LiveData<String> = _uiState
    fun addStudent(addedStudentDataItem: AddedStudentDataItem, context: Context) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.addStudent(addedStudentDataItem)
                when (response.code()) {
                    400 -> _uiState.postValue("Bunday login mavjud, boshqa yozing.")
                    500 -> _uiState.postValue("Server bilan muammo, iltimos qaytadan urinib ko'ring.")
                    200 -> {
                        _uiState.postValue("Qo‘shildi.")
                        studentlarList.add(response.body()!!)
                    }
                    else -> _uiState.postValue("Noma’lum xatolik.")
                }
            } catch (e: Exception) {
                _uiState.postValue("Xatolik: ${e.message}")
                Log.d(TAG, "addStudent: ${e.message}")
            }
        }
    }
}