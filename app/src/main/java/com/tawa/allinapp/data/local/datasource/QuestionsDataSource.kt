package com.tawa.allinapp.data.local.datasource


import com.tawa.allinapp.data.local.dao.QuestionsDao
import com.tawa.allinapp.data.local.models.*
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.GetIdUser
import javax.inject.Inject


class QuestionsDataSource @Inject constructor(private val questionsDao: QuestionsDao){

    fun insertQuestions(questionModel: QuestionModel){
        questionsDao.insertQuestions(questionModel)
    }

    fun insertAnswers(answerModel: AnswerModel){
        questionsDao.insertAnswers(answerModel)
    }

    fun insertReadyAnswers(readyAnswerModel: ReadyAnswerModel){
        questionsDao.insertReadyAnswers(readyAnswerModel)
    }

    fun insertAnswersPv(answersPvModel: AnswersPvModel){
        questionsDao.insertAnswersPv(answersPvModel)
    }

    fun getQuestions():List<QuestionModel> {
        return questionsDao.getQuestions()
    }

    fun getAudioQuestions():List<QuestionModel> {
        return questionsDao.getAudioQuestions()
    }

    fun getQuestionsByIdReport(idReport:String):List<QuestionModel> {
        return questionsDao.getQuestionsByIdReport(idReport)
    }

    fun getAnswers(idQuestion:String):List<AnswerModel> {
        return questionsDao.getAnswers(idQuestion)
    }

    fun getReadyAnswers(idQuestion:String):List<ReadyAnswerModel> {
        return questionsDao.getReadyAnswers(idQuestion)
    }

    fun getReadyAnswersImage(idQuestion:String):String {
        return questionsDao.getReadyAnswersImage(idQuestion)
    }

    fun updateAnswers(idAnswer:String,data:String){
        questionsDao.updateAnswers(idAnswer,data)
    }

    fun getCountPvAnswers(idPv:String,idQuestion:String):Int{
        return questionsDao.getCountPvAnswers(idPv,idQuestion)
    }

    fun getCountPvAnswer(idPv:String,idAnswer: String,idUser: String):Int{
        return questionsDao.getCountPvAnswer(idPv,idAnswer,idUser)
    }

    fun getAnswersPv(idQuestion: String,idPv: String,idUser: String):List<AnswerModel>{
        return questionsDao.getAnswersPv(idQuestion,idPv,idUser)
    }

    fun updateAnswersPv(idAnswer:String,idPv: String,nameAnswers:String,img:String,idUser: String){
        questionsDao.updateAnswersPv(idAnswer,idPv,nameAnswers,img,idUser)
    }


}