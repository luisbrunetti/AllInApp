package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.ScheduleRemote
import com.tawa.allinapp.data.remote.entities.TranslateRemote
import com.tawa.allinapp.models.Translate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ParametersApi {

    @GET("/api/empresa/user/get")
    fun getCompanies(@Header("Authorization") token: String): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("api/cronograma/get")
    fun getSchedule(@Header("Authorization") token: String, @Query("id_empresa") company:String): Call<BaseResponse<List<ScheduleRemote.Response>>>

    @GET("0da1e323-a068-4566-810d-d60b44c3f042/")
    fun getLanguage(): Call<BaseResponse<Translate>>
}