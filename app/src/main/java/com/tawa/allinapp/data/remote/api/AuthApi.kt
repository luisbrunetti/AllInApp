package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.LoginRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("https://run.mocky.io/v3/60296a92-d8b3-4d06-8b94-2e5b54176a62")
    fun login(@Body request: LoginRemote.Request): Call<BaseResponse<LoginRemote.Response>>

}