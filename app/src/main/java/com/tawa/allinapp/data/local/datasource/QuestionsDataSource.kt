package com.tawa.allinapp.data.local.datasource


import com.tawa.allinapp.data.local.dao.QuestionsDao
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
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
}