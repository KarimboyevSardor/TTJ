package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedTadbir
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.YigilishlarYangilashOchirishAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
import com.example.talabalarniroyxatgaolish.utils.Utils.tadbirlarList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YigilishlarniYangilashOchirishAdminVm : ViewModel() {
    private val TAG = "YIGILISHLARNIYANGILASHOCHIRISHADMINVIEWMODEL"
    private val stateYigilish = MutableStateFlow<Resource<AddedTadbir>>(Resource.Loading())
    val _stateYigilish: MutableStateFlow<Resource<AddedTadbir>> get() = stateYigilish
    private val stateDeleteYigilish = MutableStateFlow<Resource<Message>>(Resource.Loading())
    val _stateDeleteYigilish: MutableStateFlow<Resource<Message>> get() = stateDeleteYigilish
    fun editYigilish(id: Long, time: RequestBody, description: RequestBody, name: RequestBody, meeting_place: RequestBody, image: MultipartBody.Part?, context: Context, activity: FragmentActivity) {
        val editYigilish = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(activity)[LiveDates::class]
        try {
            viewModelScope.launch {
                editYigilish.editYigilish(id, image, name, time, description, meeting_place).enqueue(object : Callback<AddedTadbir>{
                    override fun onResponse(
                        call: Call<AddedTadbir>,
                        response: Response<AddedTadbir>
                    ) {
                        if (response.isSuccessful) {
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
                            tadbirlarList[tadbirlarList.indexOf(tadbirlarList.filter { it.id == yigilish.id }[0])] = yigilish
                            liveDates.tarbirlarLiveData.value = tadbirlarList
                            Toast.makeText(
                                activity,
                                "Ma'lumot saqlandi.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                        }
                    }

                    override fun onFailure(call: Call<AddedTadbir>, t: Throwable) {

                    }
                })
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteYigilish(id: Long, context: Context) {
        val editYigilishRep = YigilishlarYangilashOchirishAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                editYigilishRep.deleteYigilish(id = id)
                    .catch {
                        stateDeleteYigilish.emit(Resource.Error(it))
                    }
                    .collect{
                        stateDeleteYigilish.emit(Resource.Success(it))
                    }
            }
        } catch (e: Exception) {
            throw  e
        }
    }

    fun deleteRate(context: Context, id: Long, chip: Chip, chip_group: ChipGroup) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        try {
            apiService.deleteRate(id).enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if (response.isSuccessful) {
                        rateList.remove(rateList.filter { it.id == id }[0])
                        chip_group.removeView(chip)
                    } else {
                        Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        } catch (e: Exception) {
            throw e
        }
    }

    fun addRate(context: Context, addRep: AddRateReq, requireActivity: FragmentActivity) {
        val apiService = ApiClient.getRetrofit(context).create(ApiService::class.java)
        val liveDates = ViewModelProvider(requireActivity)[LiveDates::class]
        try {
            apiService.addRate(addRep.addRateReq).enqueue(object : Callback<RateData> {
                override fun onResponse(call: Call<RateData>, response: Response<RateData>) {
                    if (response.isSuccessful) {
                        rateList = response.body()!!.rate.toMutableList()
                        liveDates.rateLiveData.value = rateList
                        Log.d(TAG, "onResponse: ${rateList.size}")
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