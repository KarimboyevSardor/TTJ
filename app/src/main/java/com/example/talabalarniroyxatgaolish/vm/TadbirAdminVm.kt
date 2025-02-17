package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.BaholashViewPagerAdminRep
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TadbirAdminVm : ViewModel() {
    private val TAG = "TADBIRADMINVIEWMODEL"
    fun getRateMeetingId(context: Context, meeting_id: Long, activity: FragmentActivity) {
        val liveDates = ViewModelProvider(activity)[LiveDates::class.java]
        val baholashViewPagerAdminRep = BaholashViewPagerAdminRep(
            ApiClient.getRetrofit(context).create(
                ApiService::class.java))
        viewModelScope.launch {
            try {
                baholashViewPagerAdminRep.getRateMeetingId(meeting_id)
                    .catch {
                        Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    .collect {
                        liveDates.baholashLiveData.value = it
                    }
            } catch (e: Exception) {
                Log.d(TAG, "getRateMeetingId: ${e.message}")
            }
        }
    }
}