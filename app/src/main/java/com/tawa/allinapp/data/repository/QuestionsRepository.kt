package com.tawa.allinapp.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.models.Question
import javax.inject.Inject

interface QuestionsRepository {
    fun setQuestions(): Either<Failure, Boolean>
    fun getQuestions(): Either<Failure,List<Question>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val questionsDataSource: QuestionsDataSource,
                        private val prefs: Prefs,
                        private val service: QuestionsService,
    ): QuestionsRepository{

        override fun setQuestions(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getQuestions().execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            questionsDataSource.insertQuestions(it.toModel())
                                            for(answers in it.answers )
                                                questionsDataSource.insertAnswers(answers.toModel()) }
                                        Either.Right(true)
                                    }
                                    else Either.Left(Failure.DefaultError(body.message))
                                }?: Either.Left(Failure.DefaultError(""))
                            }
                            false -> Either.Left(Failure.ServerError)
                        }
                    } catch (e: Exception) {
                        Either.Left(Failure.DefaultError(e.message!!))
                    }
                }
                false -> Either.Left(Failure.NetworkConnection)
            }
        }


        override fun getQuestions(): Either<Failure, List<Question>> {
            return try {
                Either.Right(questionsDataSource.getQuestions().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


    }
}