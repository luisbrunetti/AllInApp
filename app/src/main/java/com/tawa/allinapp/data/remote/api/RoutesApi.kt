package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import com.tawa.allinapp.data.remote.entities.ReportsStatusRemote
import com.tawa.allinapp.data.remote.entities.RoutesRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RoutesApi{

    @GET("/api/cronograma/rutas/get")
    fun getRoutes(@Header("Authorization") token:String, @Query("id_empresa") idCompany:String,@Query("id_user_asig") idUser:String,@Query("date_start") dateStart:String): Call<BaseResponse<List<RoutesRemote.Response>>>

    @GET("/api/group/user/list/")
    fun getListUser(@Header("Authorization") token:String): Call<BaseResponse<List<RoutesRemote.ListUser>>>
}