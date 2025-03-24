package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.data.StudentData
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.XonaAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.studentlarList
import com.example.talabalarniroyxatgaolish.utils.Utils.xonalarList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class XonaAdminVm : ViewModel() {
    private val TAG = "XonaAdminVm"
    private val xonaDelete = MutableStateFlow<Resource<Message>>(Resource.Loading())
    val _xonaDelete: MutableStateFlow<Resource<Message>> get() = xonaDelete
    private val students = MutableStateFlow<Resource<StudentData>>(Resource.Loading())
    val _students: MutableStateFlow<Resource<StudentData>> get() = students
    fun xonaEdit(context: Context, xona: XonaDataItem) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                apiService.editXona(xona).enqueue(object : Callback<XonaDataItem>{
                    override fun onResponse(
                        call: Call<XonaDataItem>,
                        response: Response<XonaDataItem>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Xona nomi o'zgartirildi.", Toast.LENGTH_SHORT)
                                .show()
                            val indexXona = xonalarList.indexOf(xonalarList.filter { it.id == xona.id }[0])
                            xonalarList[indexXona] = response.body()!!
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<XonaDataItem>, t: Throwable) {
                        Toast.makeText(context, "Server bilan bog'lanib bo'lmadi.", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "xonaEdit: ${e.message}")
            }
        }
    }

    fun xonaDelete(context: Context, id: Long, activity: FragmentActivity) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val vm = ViewModelProvider(activity)[LiveDates::class]
        viewModelScope.launch {
            apiService.deleteXona(id).enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        activity.onBackPressed()
                        xonalarList.remove(xonalarList.filter { it.id == id }[0])
                        vm.xonalarLiveData.value = xonalarList
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(context, "Server bilan bog'lanib bo'lmadi", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    fun getStudentRoomId(context: Context) {
        val xonaAdminRep = XonaAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        viewModelScope.launch {
            try {
                xonaAdminRep.getStudentRoomId()
                    .catch {
                        students.emit(Resource.Error(it))
                    }
                    .collect{
                        students.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "xonaEdit: ${e.message}")
            }
        }
    }
    fun updateStudent(context: Context, fr: FragmentActivity, studentDataItem: StudentDataItem) {
        val liveDates = ViewModelProvider(fr)[LiveDates::class]
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                apiService.updateStudent(studentDataItem).enqueue(object : Callback<StudentDataItem>{
                    override fun onResponse(
                        call: Call<StudentDataItem>,
                        response: Response<StudentDataItem>
                    ) {
                        if (response.isSuccessful) {
                            val studentIndex = studentlarList.indexOf(studentlarList.filter { it.id == studentDataItem.id }[0])
                            studentlarList[studentIndex] = response.body()!!
                            liveDates.studentlarLiveData.value = studentlarList
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<StudentDataItem>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Server bilan bog'lanib bo'lmadi",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "updateStudent: ${e.message}")
            }
        }
    }
}