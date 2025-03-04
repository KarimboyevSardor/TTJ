package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.AdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.adminsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminVm : ViewModel() {
    private val TAG = "ADMINVIEWMODEL"
    private val adminlar = MutableStateFlow<Resource<MutableList<AdminDataItem>>>(Resource.Loading())
    val _adminlar: MutableStateFlow<Resource<MutableList<AdminDataItem>>> get() = adminlar

    suspend fun getAdmins(context: Context) {
        val adminRep = AdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        viewModelScope.apply {
            try {
                adminRep.getAdmin()
                    .catch {
                        adminlar.emit(Resource.Error(it))
                    }
                    .collect {
                        adminlar.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getAdmins: ${e.message}")
            }
        }
    }

    fun deleteAdmin(context: Context, activity: FragmentActivity, id: Long) {
        val adminRep = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class.java]
        viewModelScope.apply {
            try {
                adminRep.deleteAdmin(id).enqueue(object : Callback<Message> {
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            adminsList.remove(adminsList.filter { it.id == id }[0])
                            liveDates.adminlarLiveData.value = adminsList
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "deleteAdmin: ${e.message}")
            }
        }
    }
}