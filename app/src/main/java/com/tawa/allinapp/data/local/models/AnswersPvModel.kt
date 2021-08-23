package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.AnswersPv
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.ReportPv

@Entity(tableName = "answers_pv")
data class AnswersPvModel(
    @PrimaryKey (autoGenerate = true)  val id: Int,
    val idPv: String,
    val idAnswers: String,
    val idQuestion: String,
    val nameAnswer: String,
    val img:String,
    val idUser:String
){
    fun toView() = AnswersPv(id,idPv,idAnswers,idQuestion,nameAnswer,img,idUser)
}