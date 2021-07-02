package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReportsApi {

    @GET("https://run.mocky.io/v3/0b8161c3-fa63-4bcd-9a7f-6c4cccff080e")
    fun getReports(): Call<BaseResponse<List<ReportsRemote.Response>>>

}