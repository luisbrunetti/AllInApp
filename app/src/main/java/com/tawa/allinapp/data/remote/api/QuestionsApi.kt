package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface QuestionsApi{

    @GET("/api/pregunta/reporte/60fb1c398b978f3c44e4ad11")
    fun getQuestions(@Header("Authorization") token:String): Call<BaseResponse<List<QuestionsRemote.Response>>>

    @GET("/api/pregunta/reporte/61080334ad6bca97e82d94a9")
    fun getAudioQuestion(@Header("Authorization") token:String): Call<BaseResponse<List<QuestionsRemote.Response>>>

}