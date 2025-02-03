package com.example.talabalarniroyxatgaolish.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talabalarniroyxatgaolish.data.Auth
import com.example.talabalarniroyxatgaolish.network.ApiClient
import com.example.talabalarniroyxatgaolish.network.ApiService
import com.example.talabalarniroyxatgaolish.repository.LoginRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginVm : ViewModel() {
    private val stateLogin = MutableStateFlow<Resource<Auth>>(Resource.Loading())
    val _stateLogin: MutableStateFlow<Resource<Auth>> get() = stateLogin
    fun auth(login: String, password: String, context: Context) {
        val loginRep = LoginRep(ApiClient.getRetrofit(context).create(ApiService::class.java))
        try {
            viewModelScope.launch {
                loginRep.auth(login, password).catch {
                    stateLogin.emit(Resource.Error(it))
                }.collect{
                    stateLogin.emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

}