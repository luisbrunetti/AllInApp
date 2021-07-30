package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.*
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

    @GET("/api/fotografico/masivo/add")
    fun syncPhotoReports(@Header("Authorization") token:String,@Body request:List<ReportsPhotoRemote.Request>): Call<BaseResponse<List<ReportsPhotoRemote.Response>>>


    @PUT("/api/user/estado")
    fun updateStatus(@Header("Authorization") token: String, @Body  request: UpdateStatusRemote.Request):Call<BaseResponse<UpdateStatusRemote.Response>>

    @GET("/api/reportepdv/1/")
    fun getReportsSku(@Header("Authorization") token: String): Call<BaseResponse<List<ReportsSkuRemote.Response>>>

}