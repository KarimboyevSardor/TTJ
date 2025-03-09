package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AdminDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.utils.Utils.adminsList
import kotlinx.coroutines.launch

class AddAdminVm : ViewModel() {
    private val TAG = "ADDADMINVIEWMODEL"
    fun addAdmin(context: Context, activity: FragmentActivity, admin: AdminDataItem) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class.java]
        viewModelScope.launch {
            try {
                val response = apiService.addAdmin(admin)
                if (response.code() == 200) {
                    adminsList.add(response.body()!!)
                    liveDates.adminlarLiveData.value = adminsList
                    activity.onBackPressed()
                } else if (response.code() == 400) {
                    Toast.makeText(context, "Bunday login bor iltimos boshqa yozing.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.d(TAG, "addAdmin catch: ${e.message}")
            }
        }
    }
}