package com.tawa.allinapp.data.local.datasource


import com.tawa.allinapp.data.local.dao.QuestionsDao
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import javax.inject.Inject


class QuestionsDataSource @Inject constructor(private val questionsDao: QuestionsDao){

    fun insertQuestions(questionModel: QuestionModel){
        questionsDao.insertQuestions(questionModel)
    }

    fun insertAnswers(answerModel: AnswerModel){
        questionsDao.insertAnswers(answerModel)
    }

    fun getQuestions():List<QuestionModel> {
        return questionsDao.getQuestions()
    }

    fun getAnswers(idQuestion:String):List<AnswerModel> {
        return questionsDao.getAnswers(idQuestion)
    }

}