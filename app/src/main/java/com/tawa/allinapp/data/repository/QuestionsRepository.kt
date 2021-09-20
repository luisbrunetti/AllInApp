package com.tawa.allinapp.data.repository


import android.util.Log
import com.tawa.allinapp.core.functional.Either
import com.tawa.allinapp.core.functional.Failure
import com.tawa.allinapp.core.functional.NetworkHandler
import com.tawa.allinapp.data.local.Prefs
import com.tawa.allinapp.data.local.datasource.QuestionsDataSource
import com.tawa.allinapp.data.local.datasource.ReportsDataSource
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.AnswersPvModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.service.QuestionsService
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.AudioReport
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.ReadyAnswer
import javax.inject.Inject

interface QuestionsRepository {
    fun setQuestions(idReport: String): Either<Failure, Boolean>
    fun setAudioQuestion(): Either<Failure, Boolean>
    fun setReadyAnswers(id: Int, idQuestion: String, nameQuestion: String, idAnswer : String, nameAnswer: String, img : String): Either<Failure, Boolean>
    fun getQuestions(idReport: String): Either<Failure,List<Answer>>
    fun getAnswers(idQuestion: String): Either<Failure,List<Answer>>
    fun updateAnswers(idAnswer:String,data:String): Either<Failure, Boolean>
    fun changeState(state: Boolean,verify:Boolean):Either<Failure, Boolean>
    fun setAnswersPv(idAnswer: String,idQuestion: String,nameAnswer: String,img: String):Either<Failure, Boolean>
    fun getAudioQuestions(): Either<Failure,List<Question>>

    class Network
    @Inject constructor(private val networkHandler: NetworkHandler,
                        private val questionsDataSource: QuestionsDataSource,
                        private val reportsDataSource: ReportsDataSource,
                        private val prefs: Prefs,
                        private val service: QuestionsService,
    ): QuestionsRepository{

        override fun setQuestions(idReport: String): Either<Failure, Boolean> {
            return when (networkHandler.isConnected) {
                true ->{
                    try {
                        val response = service.getQuestions("Bearer ${prefs.token!!}",idReport).execute()
                        when (response.isSuccessful) {
                            true -> {
                                response.body()?.let { body ->
                                    if(body.success) {
                                        body.data.map {
                                            Log.d("PREGUNTAS",it.toString())
                                            questionsDataSource.insertQuestions(it.question.toModel())
                                            for(answers in it.answers )
                                                questionsDataSource.insertAnswers(AnswerModel(answers.id,answers.status,answers.answerName,answers.order,answers.idQuestion,answers.idUserCreator,answers.idUserModifier,answers.dateModify,answers.dateCreation,"",it.question.questionName,it.question.objectType,answers.row,answers.column,it.question.order.toInt().toString(),it.question.required))
                                            it.subAnswers.map { sub ->
                                                if (sub != null) {
                                                    questionsDataSource.insertAnswers(AnswerModel(sub.id,"",sub.subAnswerName,sub.order,sub.idQuestion,"","","","","",it.question.questionName,it.question.objectType,false,false,it.question.order.toInt().toString(),it.question.required))
                                                }
                                            }
                                        }
                                        Either.Right(true)
                                    }
                                    else {
                                        Either.Left(Failure.DefaultError(body.message))
                                    }
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

        override fun setReadyAnswers(id: Int, idQuestion: String, nameQuestion: String, idAnswer : String, nameAnswer: String, img : String): Either<Failure, Boolean> {
            return try {
                questionsDataSource.insertReadyAnswers(ReadyAnswerModel(id,idQuestion,nameQuestion,idAnswer,nameAnswer,img,prefs.pvId?:""))
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }


        override fun getQuestions(idReport :String): Either<Failure, List<Answer>> {
            return try {
                val questions = questionsDataSource.getQuestionsByIdReport(idReport).map { it.toView() }
                val listAnswers = mutableListOf<Answer>()
               questions.forEach {
                   val count  = questionsDataSource.getCountPvAnswers(prefs.pvId?:"",it.id)
                   if(count==0)
                      listAnswers.addAll(questionsDataSource.getAnswers(it.id).map { it.toView() })
                   else
                      listAnswers.addAll(questionsDataSource.getAnswersPv(it.id,prefs.pvId?:"",prefs.idUser?:"").map { it.toView() })
                    }
                Either.Right(listAnswers)
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
                val count  = questionsDataSource.getCountPvAnswers(prefs.pvId?:"",idQuestion)
                if(count==0)
                    Either.Right(questionsDataSource.getAnswers(idQuestion).map { it.toView() })
                else
                    Either.Right(questionsDataSource.getAnswersPv(idQuestion,prefs.pvId?:"",prefs.idUser?:"").map { it.toView() })

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

        override fun setAnswersPv(idAnswer: String, idQuestion: String, nameAnswer: String, img: String): Either<Failure, Boolean> {
            return try {
                val count = questionsDataSource.getCountPvAnswer(prefs.pvId?:"",idAnswer,prefs.idUser?:"")
                if(count==0)
                    questionsDataSource.insertAnswersPv(AnswersPvModel(0,prefs.pvId?:"",idAnswer,idQuestion,nameAnswer,img,prefs.idUser?:""))
                else
                    questionsDataSource.updateAnswersPv(idAnswer,prefs.pvId?:"",nameAnswer,img,prefs.idUser?:"")
                Either.Right(true)
            }catch (e:Exception){
                Either.Left(Failure.DefaultError(e.message!!))
            }
        }
    }
}
