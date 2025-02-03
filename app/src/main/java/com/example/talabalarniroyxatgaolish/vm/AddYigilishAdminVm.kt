package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddEditYigilish
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedYigilish
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.AddYigilishAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddYigilishAdminVm : ViewModel() {
    private val TAG = "ADDYIGILISHADMINVIEWMODEL"
    private val stateAddYigilish = MutableStateFlow<Resource<AddedYigilish>>(Resource.Loading())
    val _stateAddYigilish: MutableStateFlow<Resource<AddedYigilish>> get() = stateAddYigilish
    fun addYigilish(time: RequestBody, description: RequestBody, name: RequestBody, meetingPlace: RequestBody, image: MultipartBody.Part?, context: Context) {
        val addYigilishAdminRep = AddYigilishAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                addYigilishAdminRep.addYigilish(image, name, time, description, meetingPlace)
                    .catch {
                        stateAddYigilish.emit(Resource.Error(it))
                    }
                    .collect {
                        stateAddYigilish.emit(Resource.Success(it))
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
    fun addRate(context: Context, addRep: AddRateReq, requireActivity: FragmentActivity) {
        val liveDates = ViewModelProvider(requireActivity)[LiveDates::class]
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        try {
            apiService.addRate(addRep.addRateReq).enqueue(object : Callback<RateData> {
                override fun onResponse(call: Call<RateData>, response: Response<RateData>) {
                    if (response.isSuccessful) {
                        rateList = response.body()!!.rate.toMutableList()
                        liveDates.rateLiveData.value = rateList
                    } else {
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<RateData>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            throw e
        }
    }
}