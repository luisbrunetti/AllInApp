package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.entities.QuestionsRemote

@Dao
interface QuestionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertQuestions(questionModel: QuestionModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAnswers(answerModel: AnswerModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReadyAnswers(readyAnswerModel: ReadyAnswerModel)

    @Query("SELECT * FROM questions order by `order` asc")
    fun getQuestions(): List<QuestionModel>

    @Query("SELECT * FROM questions WHERE questionName='AUDIO' ")
    fun getAudioQuestions(): List<QuestionModel>

    @Query("SELECT * FROM questions WHERE idReport =:idReport")
    fun getQuestionsByIdReport(idReport:String): List<QuestionModel>

    @Query("SELECT * FROM answers where idQuestion=:idQuestion")
    fun getAnswers(idQuestion : String): List<AnswerModel>

    @Query("SELECT * FROM ready_answers where idQuestion=:idQuestion")
    fun getReadyAnswers(idQuestion : String): List<ReadyAnswerModel>

    @Query("SELECT distinct(img) FROM ready_answers where idQuestion=:idQuestion")
    fun getReadyAnswersImage(idQuestion : String): String

    @Query("UPDATE answers set data=:data where id=:idAnswer")
    fun updateAnswers(idAnswer:String,data:String)
}