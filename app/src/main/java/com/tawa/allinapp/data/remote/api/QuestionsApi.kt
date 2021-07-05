package com.tawa.allinapp.data.remote.api

import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.entities.ReportsRemote
import retrofit2.Call
import retrofit2.http.GET

interface QuestionsApi{

    @GET("https://run.mocky.io/v3/4182a98f-5caa-4fc4-a691-269474e5c2a3")
    fun getQuestions(): Call<BaseResponse<List<QuestionsRemote.Response>>>

}