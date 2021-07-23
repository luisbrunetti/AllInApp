package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET

interface QuestionsApi{

    @GET("/api/pregunta/reporte/60f62ddfa6ac481ddcf23bdb")
    fun getQuestions(): Call<BaseResponse<List<QuestionsRemote.Response>>>

}