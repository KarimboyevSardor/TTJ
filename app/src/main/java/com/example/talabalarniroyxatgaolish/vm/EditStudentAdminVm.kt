package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddedStudentDataItem
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditStudentAdminVm : ViewModel() {
    private val TAG = "EDITSTUDENTADMINVIEWMODEL"
    private val editStudent = SingleLiveEvent<String>()
    val _editStudent get() = editStudent
    private val auth = MutableStateFlow<Resource<AuthDataItem>>(Resource.Loading())
    val _auth: MutableStateFlow<Resource<AuthDataItem>> get() = auth

    fun EditAuth(context: Context, addedStudentDataItem: AddedStudentDataItem) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.editAuthStudent(AuthDataItem(id = addedStudentDataItem.auth_id,
                    login = addedStudentDataItem.login, password = addedStudentDataItem.password, role = "student"))
                when(response.code()) {
                    500 -> editStudent.postValue("Bunday login bor boshqa yozing.")
                    200 -> {
                        editStudent(context, addedStudentDataItem)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "EditAuth: ${e.message}")
            }
        }
    }

    fun editStudent(context: Context, addedStudentDataItem: AddedStudentDataItem) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                val response = client.editStudent(StudentDataItem(
                    auth_id = addedStudentDataItem.auth_id,
                    course_count = addedStudentDataItem.course_count,
                    course = addedStudentDataItem.course,
                    id = addedStudentDataItem.id,
                    room_id = addedStudentDataItem.room_id,
                    name = addedStudentDataItem.name
                ))
                when(response.code()) {
                    200 -> {
                        studentlarList[studentlarList.indexOf(studentlarList.filter { it.id == addedStudentDataItem.id }[0])] = response.body()!!
                        editStudent.postValue("update")
                    }
                    500 -> editStudent.postValue("Server bilan ulanib bo'lmadi qaytadan uruning.")
                }
            } catch (e: Exception) {
                Log.d(TAG, "EditAuth: ${e.message}")
            }
        }
    }

    fun getAuth(context: Context, id: Long) {
        val client = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            client.getAuth(id)
                .catch {
                    auth.emit(Resource.Error(it))
                }
                .collect {
                    auth.emit(Resource.Success(it))
                }
        }
    }
}