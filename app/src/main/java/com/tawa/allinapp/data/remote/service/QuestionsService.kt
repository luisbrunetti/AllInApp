package com.tawa.allinapp.data.remote.service


import com.tawa.allinapp.data.remote.api.QuestionsApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class QuestionsService
@Inject constructor(retrofit: Retrofit) : QuestionsApi {
    private val questionsApi by lazy { retrofit.create(QuestionsApi::class.java) }

    override fun getQuestions() = questionsApi.getQuestions()

}