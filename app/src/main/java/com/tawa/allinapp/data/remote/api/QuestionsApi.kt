package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET

interface QuestionsApi{

    @GET("https://run.mocky.io/v3/95921c44-d505-4860-bce4-82cb45765e9b")
    fun getQuestions(): Call<BaseResponse<List<QuestionsRemote.Response>>>

}