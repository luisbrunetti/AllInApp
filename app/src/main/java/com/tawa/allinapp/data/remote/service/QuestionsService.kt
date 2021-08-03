package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.core.platform.BaseResponse
import com.tawa.allinapp.data.remote.api.QuestionsApi
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QuestionsService
@Inject constructor(retrofit: Retrofit) : QuestionsApi {
    private val questionsApi by lazy { retrofit.create(QuestionsApi::class.java) }

    override fun getQuestions(token:String) = questionsApi.getQuestions(token)

    override fun getAudioQuestion(token: String) =  questionsApi.getAudioQuestion(token)
}