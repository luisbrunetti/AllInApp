package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface QuestionsApi{

    @GET("/api/pregunta/reporte/{id}")
    fun getQuestions(@Header("Authorization") token:String, @Path("id") idReport:String): Call<BaseResponse<List<QuestionsRemote.Response>>>

    @GET("/api/pregunta/reporte/61080334ad6bca97e82d94a9")
    fun getAudioQuestion(@Header("Authorization") token:String): Call<BaseResponse<List<QuestionsRemote.Response>>>

}