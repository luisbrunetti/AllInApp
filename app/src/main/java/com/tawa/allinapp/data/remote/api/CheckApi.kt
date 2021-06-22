package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

interface CheckApi {
    @GET("https://run.mocky.io/v3/09093793-a6bf-4ba8-b413-886fdfa0a623")
    fun sendChecks(): Call<BaseResponse<Boolean>>
}