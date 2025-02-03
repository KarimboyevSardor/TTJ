package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.AddRateReq
import com.example.talabalarniroyxatgaolish.data.AddedYigilish
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.YigilishlarYangilashOchirishAdminRep
import com.example.talabalarniroyxatgaolish.utils.Utils.rateList
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
    private val stateYigilish = MutableStateFlow<Resource<AddedYigilish>>(Resource.Loading())
    val _stateYigilish: MutableStateFlow<Resource<AddedYigilish>> get() = stateYigilish
    private val stateDeleteYigilish = MutableStateFlow<Resource<Message>>(Resource.Loading())
    val _stateDeleteYigilish: MutableStateFlow<Resource<Message>> get() = stateDeleteYigilish
    fun editYigilish(id: Long, time: RequestBody, description: RequestBody, name: RequestBody, meeting_place: RequestBody, image: MultipartBody.Part?, context: Context) {
        val editYigilishRep = YigilishlarYangilashOchirishAdminRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                editYigilishRep.editYigilish(id = id, image = image, name = name, time = time, description = description, meeting_place = meeting_place)
                    .catch {
                        stateYigilish.emit(Resource.Error(it))
                    }
                    .collect {
                        stateYigilish.emit(Resource.Success(it))
                    }
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
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
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