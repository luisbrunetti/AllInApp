package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.*
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
    fun getChains(@Header("Authorization") token: String, @Query("id_empresa") company:String, @Query("id_canal") channel:List<String>, @Query("id_retail") retail:List<String>): Call<BaseResponse<List<ChainRemote.Response>>>

    @GET("/api/group/user/list/")
    fun getUserList(@Header("Authorization") token: String, @Query("empresas") company:String): Call<BaseResponse<List<UserRemote.Response>>>

    @GET("/api/estadisticas/")
    fun getGraph(
        @Header("Authorization") token: String,
        @Query("id_empresa") company:String?,
        @Query("date_start") dateStart:String?,
        @Query("date_end") dateEnd:String?,
        @Query("id_user") user:List<String>?,
        @Query("id_cadena") chains:List<String>?,
    ): Call<BaseResponse<GraphRemote.Response>>
}