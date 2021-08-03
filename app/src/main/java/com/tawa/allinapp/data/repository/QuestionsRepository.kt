package com.tawa.allinapp.data.repository


import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.ReadyAnswer
import javax.inject.Inject

interface QuestionsRepository {
    fun setQuestions(): Either<Failure, Boolean>
    fun setAudioQuestion(): Either<Failure, Boolean>
    fun setReadyAnswers(readyAnswer: ReadyAnswer): Either<Failure, Boolean>
    fun getQuestions(): Either<Failure,List<Question>>
    fun getAnswers(idQuestion: String): Either<Failure,List<Answer>>
    fun updateAnswers(idAnswer:String,data:String): Either<Failure, Boolean>
    fun changeState(state: Boolean,verify:Boolean):Either<Failure, Boolean>
    fun getStateChecklist():Either<Failure, ArrayList<Boolean>>
    fun getAudioQuestions(): Either<Failure,List<Question>>

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
                        val response = service.getQuestions("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("PREGUNTAS",it.toString())
                                            questionsDataSource.insertQuestions(it.question.toModel())
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

        override fun setAudioQuestion(): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getAudioQuestion("Bearer ${prefs.token!!}").execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("AUDIO",it.toString())
                                            questionsDataSource.insertQuestions(it.question.toModel())
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

        override fun setReadyAnswers(readyAnswer: ReadyAnswer): Either<Failure, Boolean> {
            return try {
                questionsDataSource.insertReadyAnswers(readyAnswer.toModel())
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


        override fun getQuestions(): Either<Failure, List<Question>> {
            return try {
                Either.Right(questionsDataSource.getQuestions().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getAudioQuestions(): Either<Failure, List<Question>> {
            return try {
                Either.Right(questionsDataSource.getAudioQuestions().map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getAnswers(idQuestion:String): Either<Failure, List<Answer>> {
            return try {
                Either.Right(questionsDataSource.getAnswers(idQuestion).map { it.toView() })
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun updateAnswers(idAnswer: String, data: String): Either<Failure, Boolean> {

            return try {
                questionsDataSource.updateAnswers(idAnswer,data)
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun changeState(state:Boolean,verify: Boolean): Either<Failure, Boolean> {
            return try {
                prefs.stateChecklist =state
                prefs.verifyChecklist = verify
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }

        override fun getStateChecklist(): Either<Failure, ArrayList<Boolean>> {
            return try {
                val data  = arrayListOf(prefs.stateChecklist,prefs.verifyChecklist)
                Either.Right(data)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


    }
}