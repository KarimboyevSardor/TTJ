package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.R
import com.example.talabalarniroyxatgaolish.data.Auth
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.LoginRep
import com.example.talabalarniroyxatgaolish.ui.admin.BoshAdmin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginVm : ViewModel() {
    private val TAG = "LOGINVIEWMODEL"
    private val stateLogin = MutableStateFlow<Resource<Auth>>(Resource.Loading())
    val _stateLogin: MutableStateFlow<Resource<Auth>> get() = stateLogin
    fun auth(login: String, password: String, context: Context, activity: FragmentActivity) {
        val loginRep = ApiClient.getRetrofit(context).create(ApiService::class.java)
        try {
            viewModelScope.launch {
                loginRep.auth(login, password).enqueue(object : Callback<Auth>{
                    override fun onResponse(call: Call<Auth>, response: Response<Auth>) {
                        if (response.isSuccessful) {
                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, BoshAdmin())
                                .commit()
                        } else {
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Auth>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }
                })
            }
        } catch (e: Exception) {
            throw e
        }
    }

}