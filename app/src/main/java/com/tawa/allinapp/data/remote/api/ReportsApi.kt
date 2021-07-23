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

    /*@Headers(
        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJTVVBFUlZJU09SIl0sImVtcHJlc2FzIjpbIjYwZjNjOTVmMDI1NTMyNTAzODU3M2VlZSJdLCJmZV9tb2RpZmljYWNpb24iOiIyMDIxLTA3LTE4VDAwOjAwOjAwLjAwMFoiLCJpZF91c2VyX21vZGlmaWNhZG9yIjoiNjBmM2QzMzk0YTk5MjI0MWRjZmIzNmVkIiwic3RhdHVzIjoiQUNUIiwiZmVfY29uZXhpb24iOm51bGwsInVsX2xhdGl0dWQiOm51bGwsInVsX2xvYmd1aXR1ZCI6bnVsbCwiYmF0ZXJpYSI6bnVsbCwiX2lkIjoiNjBmNGE0ZTE0M2U2MTEyZjk4ZjVhYmRlIiwidXN1YXJpbyI6InN1cGVydiIsIm5vbWJyZV9hcGVsbGlkbyI6InN1cGVydmlzb3IiLCJjb250cmFzZcOxYSI6IiQyYiQxMCRPTDJGZGhpSm5WeVRXcTczUi5Yclguc2p5RDNrcHN3YzY0ZDBLSmhqRjBLcnd3SjUwL210VyIsImVtYWlsIjoic3VwZXJ2aXNvckBnbWFpbC5jb20iLCJpZF91c2VyX2NyZWFkb3IiOiI2MGYzZDMzOTRhOTkyMjQxZGNmYjM2ZWQiLCJmZV9jcmVhY2lvbiI6IjIwMjEtMDctMThUMDA6MDA6MDAuMDAwWiIsIl9fdiI6MCwiaWF0IjoxNjI2NzIzMDA4fQ.JkdiKa_pRQ7eIDrxVO5KK9f6Fd9tJzRraQtsTTfSEXg"
    )*/
    @GET("http://192.168.1.44:3000/api/group/user/list/1")
     //https://run.mocky.io/v3/cc3dc872-5d9a-4302-aac1-463ab8407e2a
    fun getReportStatus(@Header("Authorization") token:String): Call<BaseResponse<List<ReportsStatusRemote.Response>>>

}