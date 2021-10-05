package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.*
import retrofit2.Call
import retrofit2.http.*

interface PdvApi{

    @GET("/api/puntoventa/")
    fun getPdvCompany(@Header("Authorization") token:String, @Query("id_empresa") idCompany:String): Call<BaseResponse<List<PdvRemote.Response>>>

    @POST("/api/puntoventa/edit/solicitud/{id}")
    fun updatePdvRemote(@Header("Authorization") token:String, @Path("id",) idPdv:String,@Body request: PdvRemote.Request): Call<BaseResponse<String>>
}