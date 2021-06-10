package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.LoginRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("https://run.mocky.io/v3/d449f442-037d-4b62-9428-0e7a2b615690")
    fun login(@Body request: LoginRemote.Request): Call<BaseResponse<LoginRemote.Response>>

}