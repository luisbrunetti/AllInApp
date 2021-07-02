package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET

interface QuestionsApi{

    @GET("https://run.mocky.io/v3/02c951fb-17fc-4a40-935a-c6463d57a949")
    fun getQuestions(): Call<BaseResponse<List<QuestionsRemote.Response>>>

}