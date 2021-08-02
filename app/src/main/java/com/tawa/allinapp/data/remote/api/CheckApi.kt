package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.CheckRemote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CheckApi {
    @POST("/api/asistencia/ingreso/add")
    fun sendCheckIn(@Body request:CheckRemote.Request): Call<BaseResponse<CheckRemote.Response>>

    @POST("/api/asistencia/salida/add")
    fun sendCheckOut(@Body request:CheckRemote.Request): Call<BaseResponse<CheckRemote.Response>>

    @POST("/api/asistencia/masivo/add")
    fun syncChecks(@Header("Authorization") token: String, @Body request:List<CheckRemote.Request>): Call<BaseResponse<List<CheckRemote.Response>>>
}