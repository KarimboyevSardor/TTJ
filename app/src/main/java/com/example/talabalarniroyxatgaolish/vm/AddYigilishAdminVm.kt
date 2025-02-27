package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedRate
import com.example.talabalarniroyxatgaolish.data.AddedTadbir
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.AddYigilishAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
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
    private val stateAddYigilish = MutableStateFlow<Resource<AddedTadbir>>(Resource.Loading())
    val _stateAddYigilish: MutableStateFlow<Resource<AddedTadbir>> get() = stateAddYigilish
    fun addYigilish(time: RequestBody, description: RequestBody, name: RequestBody, meetingPlace: RequestBody, image: MultipartBody.Part?, context: Context, activity: FragmentActivity, qoshilganStudents: MutableList<StudentDataItem>) {
        val addYigilishAdminRep = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class]
        try {
            viewModelScope.launch {
                addYigilishAdminRep.addYigilish(image, name, time, description, meetingPlace).enqueue(object : Callback<AddedTadbir>{
                    override fun onResponse(
                        call: Call<AddedTadbir>,
                        response: Response<AddedTadbir>
                    ) {
                        if (response.isSuccessful) {
                            val rateStudents: MutableList<AddedRate> = mutableListOf()
                            for (i in 0 until qoshilganStudents.size) {
                                rateStudents.add(AddedRate(response.body()!!.meeting.id, "0", qoshilganStudents[i].id))
                            }
                            val yigilish = TadbirlarDataItem(
                                description = response.body()!!.meeting.description,
                                id = response.body()!!.meeting.id,
                                image_base64 = response.body()!!.meeting.image_base64,
                                meeting_place = response.body()!!.meeting.meeting_place,
                                name = response.body()!!.meeting.name,
                                time = response.body()!!.meeting.time,
                                image_name = "",
                                image_path = ""
                            )
                            tadbirlarList.add(yigilish)
                            liveDates.tarbirlarLiveData.value = tadbirlarList
                            val addRate = AddRateReq(rateStudents)
                            addRate(context, addRate, activity)
                            activity.onBackPressed()
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<AddedTadbir>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
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