package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CompanyRemote
import com.tawa.allinapp.data.remote.entities.LoginRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import com.tawa.allinapp.data.remote.entities.ReportsStatusRemote
import retrofit2.Call
import retrofit2.http.*

interface ReportsApi {

    @GET("https://run.mocky.io/v3/85d0d838-c388-485f-8213-87039023c0c3/{id_empr}")
    fun getReports(@Path("id_empr") company:String): Call<BaseResponse<List<ReportsRemote.Response>>>

    //TODO make multiparts with the real service
    @GET("https://run.mocky.io/v3/26d7c983-fbee-45ae-9a65-990aa317d5dd")
    fun setPhotoReports(
        before1:String,before2:String,before3:String,before4:String,before5:String,
        after1:String,after2:String,after3:String,after4:String,after5:String,comment:String
    ): Call<BaseResponse<String>>

    @GET("/api/group/user/list/1")
    fun getReportStatus(@Header("Authorization") token:String): Call<BaseResponse<List<ReportsStatusRemote.Response>>>

}