package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.ChainRemote
import com.tawa.allinapp.data.remote.entities.ChannelRemote
import com.tawa.allinapp.data.remote.entities.RetailRemote
import com.tawa.allinapp.data.remote.entities.UserRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DashboardApi {
    @GET("/api/canal/1/")
    fun getChannels(@Header("Authorization") token: String, @Query("id_empresa") company:String): Call<BaseResponse<List<ChannelRemote.Response>>>

    @GET("/api/retail/1/")
    fun getRetails(@Header("Authorization") token: String, @Query("id_empresa") company:String): Call<BaseResponse<List<RetailRemote.Response>>>

    @GET("/api/cadena/1/")
    fun getChains(@Header("Authorization") token: String, @Query("id_empresa") company:String, @Query("id_canal") channel:String, @Query("id_retail") retail:String): Call<BaseResponse<List<ChainRemote.Response>>>

    @GET("/api/group/user/list/")
    fun getUserList(@Header("Authorization") token: String, @Query("empresas") company:String): Call<BaseResponse<List<UserRemote.Response>>>
}