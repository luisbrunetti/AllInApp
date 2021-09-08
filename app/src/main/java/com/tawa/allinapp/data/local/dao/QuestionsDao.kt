package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.AnswersPvModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.local.models.ReadyAnswerModel
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.SetIdPv
import com.tawa.allinapp.models.Answer

@Dao
interface QuestionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertQuestions(questionModel: QuestionModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAnswers(answerModel: AnswerModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReadyAnswers(readyAnswerModel: ReadyAnswerModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAnswersPv(answersPvModel: AnswersPvModel)

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

    @Query("SELECT count(id) FROM answers_pv where idPv = :idPv and idQuestion=:idQuestion")
    fun getCountPvAnswers(idPv:String,idQuestion: String): Int

    @Query("SELECT count(id) FROM answers_pv where idPv = :idPv and idAnswers=:idAnswer and idUser=:idUser")
    fun getCountPvAnswer(idPv:String,idAnswer: String,idUser:String): Int

    @Query("SELECT answers.id, answers.status,answers.answerName,answers.`order`,answers.idQuestion,answers.idUserCreator,answers.idUserModifier,answers.dateModify,answers.dateCreation,answers_pv.nameAnswer as 'data',answers.nameQuestion,answers.objectType,answers.`row`,answers.`column` FROM answers, answers_pv where answers.idQuestion=answers_pv.idQuestion and answers.id=answers_pv.idAnswers and  answers.idQuestion=:idQuestion and answers_pv.idPv =:idPv and answers_pv.idUser=:idUser")
    fun getAnswersPv(idQuestion : String,idPv: String,idUser: String): List<AnswerModel>

    @Query("UPDATE answers_pv set nameAnswer=:nameAnswer, img=:img where idAnswers=:idAnswer and idPv=:idPv and idUser=:idUser")
    fun updateAnswersPv(idAnswer:String,idPv: String,nameAnswer: String,img:String,idUser: String)
}