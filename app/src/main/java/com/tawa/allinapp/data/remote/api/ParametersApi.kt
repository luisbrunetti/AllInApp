package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.ScheduleRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ParametersApi {

    @GET("/api/empresa/user/get")
    fun getCompanies(@Header("Authorization") token: String): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("/api/cronograma/1")
    fun getSchedule(@Header("Authorization") token: String): Call<BaseResponse<List<ScheduleRemote.Response>>>
}