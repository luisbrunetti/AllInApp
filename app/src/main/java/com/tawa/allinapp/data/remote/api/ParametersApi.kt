package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.LanguageRemote
import com.tawa.allinapp.data.remote.entities.ScheduleRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ParametersApi {

    @GET("/api/empresa/user/get")
    fun getCompanies(@Header("Authorization") token: String): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("api/cronograma/get")
    fun getSchedule(@Header("Authorization") token: String, @Query("id_empresa") company:String): Call<BaseResponse<List<ScheduleRemote.Response>>>

    @GET("ccaf952c-5c92-40e7-95a2-38d084a50efd/")
    fun getLanguage(@Header("Language") language: String): Call<BaseResponse<List<LanguageRemote>>>
}