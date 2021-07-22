package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.PVRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface ParametersApi {

    @GET("/api/empresa/user/get")
    fun getCompanies(@Header("Authorization") token: String): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("https://run.mocky.io/v3/de3a5087-1ecb-4a62-8cca-f5aadb3228eb")
    fun getPV(): Call<BaseResponse<List<PVRemote.Response>>>
}