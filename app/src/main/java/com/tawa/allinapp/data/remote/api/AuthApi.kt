package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.SendPassword
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/user/login")
    fun login(@Body request: LoginRemote.Request): Call<BaseResponse<LoginRemote.Response>>

    @POST("/api/user/enviar/password")
    fun sendPassword(@Header("Authorization") token: String, @Body request: SendPassword.Request): Call<BaseResponse<String>>
}