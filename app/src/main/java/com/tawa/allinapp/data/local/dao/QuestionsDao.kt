package com.tawa.allinapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.remote.entities.QuestionsRemote

@Dao
interface QuestionsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertQuestions(questionModel: QuestionModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAnswers(answerModel: AnswerModel)

    @Query("SELECT * FROM questions")
    fun getQuestions(): List<QuestionModel>




}