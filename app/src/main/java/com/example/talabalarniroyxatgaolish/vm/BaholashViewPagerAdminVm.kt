package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.BaholashViewPagerAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaholashViewPagerAdminVm : ViewModel() {
    private val TAG = "BAHOLASHVIEWPAGERADMINVIEWMODEL"
    fun editRate(context: Context, editRate: List<Rate>, activity: FragmentActivity) {
        val liveDates = ViewModelProvider(activity)[LiveDates::class.java]
        val baholashViewPagerAdminRep = BaholashViewPagerAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        viewModelScope.launch {
            try {
                baholashViewPagerAdminRep.editRate(editRate).enqueue(object : Callback<RateData>{
                    override fun onResponse(call: Call<RateData>, response: Response<RateData>) {
                        if (response.isSuccessful) {
                            rateList = response.body()!!.rate.toMutableList()
                            liveDates.rateLiveData.value = rateList
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<RateData>, t: Throwable) {
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: Exception) {
                Log.d(TAG, "editRate: ${e.message}")
            }
        }
    }
}