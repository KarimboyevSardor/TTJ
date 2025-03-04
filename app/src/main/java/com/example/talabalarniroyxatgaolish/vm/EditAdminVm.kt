package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.data.AuthData
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.data.LoginData
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.EditAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.adminsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdminVm : ViewModel() {
    private val TAG = "EDITVIEWMODEL"
    private val logins = MutableStateFlow<Resource<AuthDataItem>>(Resource.Loading())
    val _logins: MutableStateFlow<Resource<AuthDataItem>> get() = logins
    fun getLogins(context: Context, id: Long) {
        val editAdminRep = EditAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        viewModelScope.launch {
            try {
                editAdminRep.getAuth(id)
                    .catch {
                        logins.emit(Resource.Error(it))
                    }
                    .collect {
                        logins.emit(Resource.Success(it))
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getLogins: ${e.message}")
            }
        }
    }

    fun editAdmin(context: Context, activity: FragmentActivity, admin: AdminDataItem) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class]
        val position = adminsList.indexOf(adminsList.filter { it.id == admin.id }[0])
        viewModelScope.launch {
            try {
                apiService.editAdmin(admin).enqueue(object : Callback<AdminDataItem> {
                    override fun onResponse(
                        call: Call<AdminDataItem>,
                        response: Response<AdminDataItem>
                    ) {
                        if (response.isSuccessful) {
                            adminsList[position] = response.body()!!
                            liveDates.adminlarLiveData.value = adminsList
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<AdminDataItem>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "editAdmin: ${e.message}")
            }
        }
    }

    fun editAuth(context: Context, authDataItem: AuthDataItem, activity: FragmentActivity) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        viewModelScope.launch {
            try {
                apiService.editAuth(authDataItem).enqueue(object : Callback<AuthDataItem> {
                    override fun onResponse(
                        call: Call<AuthDataItem>,
                        response: Response<AuthDataItem>
                    ) {
                        if (response.isSuccessful) {
                            activity.onBackPressed()
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }
                    override fun onFailure(call: Call<AuthDataItem>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "editAdmin: ${e.message}")
            }
        }
    }
}