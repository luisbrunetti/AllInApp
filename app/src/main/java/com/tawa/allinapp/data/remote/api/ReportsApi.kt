package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportsApi {

    @GET("https://run.mocky.io/v3/ec59ea2e-bf08-40cc-b514-4ab59866491c/{id_empr}")
    fun getReports(@Path("id_empr") company:String): Call<BaseResponse<List<ReportsRemote.Response>>>

}