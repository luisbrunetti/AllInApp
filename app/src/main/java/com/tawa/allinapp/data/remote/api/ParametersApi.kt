package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.PVRemote
import retrofit2.Call
import retrofit2.http.GET

interface ParametersApi {
    @GET("https://run.mocky.io/v3/32c5c115-a349-4676-b248-37f3206e8098")
    fun getCompanies(): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("https://run.mocky.io/v3/b007c88e-0907-409b-8f52-ae8b1d84e562")
    fun getPV(): Call<BaseResponse<List<PVRemote.Response>>>
}