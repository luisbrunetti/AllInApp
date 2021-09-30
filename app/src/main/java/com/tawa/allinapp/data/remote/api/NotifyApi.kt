package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.*
import retrofit2.Call
import retrofit2.http.*

interface NotifyApi{

    @GET("/api/notificacion/user/get")
    fun getNotify(@Header("Authorization") token:String): Call<BaseResponse<List<NotifyRemote.Response>>>
}