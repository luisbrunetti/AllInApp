package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.PVRemote
import retrofit2.Call
import retrofit2.http.GET

interface ParametersApi {
    @GET("https://run.mocky.io/v3/32c5c115-a349-4676-b248-37f3206e8098")
    fun getCompanies(): Call<BaseResponse<List<CompanyRemote.Response>>>

    @GET("https://run.mocky.io/v3/de3a5087-1ecb-4a62-8cca-f5aadb3228eb")
    fun getPV(): Call<BaseResponse<List<PVRemote.Response>>>
}