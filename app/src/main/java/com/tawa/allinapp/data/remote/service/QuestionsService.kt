package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.QuestionsApi
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.GetIdCompany
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QuestionsService
@Inject constructor(retrofit: Retrofit) : QuestionsApi {
    private val questionsApi by lazy { retrofit.create(QuestionsApi::class.java) }

    override fun getQuestions(token:String,idCompany: String) = questionsApi.getQuestions(token,idCompany)

    override fun getAudioQuestion(token: String) =  questionsApi.getAudioQuestion(token)
}