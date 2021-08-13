package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.ChannelRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DashboardApi {
    @GET("/api/canal/1/")
    fun getChannels(@Header("Authorization") token: String, @Query("id_empresa") company:String): Call<BaseResponse<List<ChannelRemote.Response>>>
}