package com.example.talabalarniroyxatgaolish.network

import com.example.talabalarniroyxatgaolish.data.AddedRate
import com.example.talabalarniroyxatgaolish.data.AddedTadbir
import com.example.talabalarniroyxatgaolish.data.Auth
import com.example.talabalarniroyxatgaolish.data.AuthDataItem
import com.example.talabalarniroyxatgaolish.data.DavomatData
import com.example.talabalarniroyxatgaolish.data.DavomatDataItem
import com.example.talabalarniroyxatgaolish.data.Message
import com.example.talabalarniroyxatgaolish.data.Rate
import com.example.talabalarniroyxatgaolish.data.RateData
import com.example.talabalarniroyxatgaolish.data.StudentData
import com.example.talabalarniroyxatgaolish.data.StudentDataItem
import com.example.talabalarniroyxatgaolish.data.XonaData
import com.example.talabalarniroyxatgaolish.data.XonaDataItem
import com.example.talabalarniroyxatgaolish.data.TadbirlarDataItem
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/{login}/{password}")
    fun auth(
        @Path("login") login: String,
        @Path("password") password: String
    ): Call<Auth>

    @GET("api/yigilish/geturl")
    fun getYigilish() : Flow<MutableList<TadbirlarDataItem>>

    @Multipart
    @POST("api/yigilish")
    fun addYigilish(
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("time") time: RequestBody,
        @Part("description") description: RequestBody,
        @Part("meeting_place") meeting_place: RequestBody
    ): Call<AddedTadbir>

    @Multipart
    @PUT("api/yigilish/{id}")
    fun editYigilish(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part?,
        @Part("name") name: RequestBody?,
        @Part("time") time: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("meeting_place") meeting_place: RequestBody?
    ): Call<AddedTadbir>

    @DELETE("api/yigilish/{id}")
    fun deleteYigilish(@Path("id") id: Long) : Flow<Message>

    @GET("api/davomat/{date}")
    fun getDateDavomat(@Path("date") month: String) : Flow<DavomatData>

    @GET("api/davomat")
    fun getDavomat() : Flow<DavomatData>

    @GET("api/rooms")
    fun getXona() : Flow<XonaData>

    @POST("api/rooms")
    fun addXona(@Body xonaDataItem: XonaDataItem) : Call<XonaDataItem>

    @PUT("api/rooms")
    fun editXona(@Body xona: XonaDataItem) : Call<XonaDataItem>

    @DELETE("api/rooms/{id}")
    fun deleteXona(@Path("id") id: Long) : Call<Message>

    @GET("api/student")
    fun getStudent() : Flow<StudentData>

    @GET("api/auth/{id}")
    fun getLoginId(@Path("id") id: Long) : Flow<AuthDataItem>

    @PUT("api/student")
    fun updateStudent(@Body student: StudentDataItem) : Call<StudentDataItem>

    @POST("api/rate")
    fun addRate(@Body addRate: List<AddedRate>) : Call<RateData>

    @GET("api/rate")
    fun getRate() : Flow<MutableList<Rate>>

    @DELETE("api/rate/{id}")
    fun deleteRate(@Path("id") id: Long) : Call<Message>

    @PUT("api/rate")
    fun editRate(@Body editRate: List<Rate>) : Call<RateData>

    @GET("api/rate/{meeting_id}")
    fun getRateMeetingId(@Path("meeting_id") meeting_id: Long) : Flow<MutableList<Rate>>

    @GET("api/davomat/{room_id}/{date}")
    fun getRoomDavomat(@Path("room_id") room_id: Long, @Path("date") date: String) : Flow<MutableList<DavomatDataItem>>

    @GET("api/student/{room_id}")
    fun getRoomStudent(@Path("room_id") room_id: Long) : Flow<MutableList<StudentDataItem>>

    @GET("api/davomat/{room_id}/{date}")
    fun getRoomDavomat1(@Path("room_id") room_id: Long, @Path("date") date: String) : Call<MutableList<DavomatDataItem>>

    @GET("api/student/{room_id}")
    fun getRoomStudent1(@Path("room_id") room_id: Long) : Call<MutableList<StudentDataItem>>

    @POST("api/davomat")
    fun setDavomat(@Body davomat: List<DavomatDataItem>) : Call<MutableList<DavomatDataItem>>

}