package com.example.talabalarniroyxatgaolish.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.thdev.network.flowcalladapterfactory.FlowCallAdapterFactory

object ApiClient {
    private val BASE_URL = "https://ttj-api-production.up.railway.app/"
    fun getRetrofit(context: Context) : Retrofit {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()
        val okHttpClient = OkHttpClient.Builder().addInterceptor(chuckerInterceptor).build()
        val retrofit = Retrofit
            .Builder()
            //.client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()
        return retrofit
    }
}