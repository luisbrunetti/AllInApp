package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.data.remote.entities.QuestionsRemote
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.models.*

@Entity(tableName = "ready_answers")
data class ReadyAnswerModel(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val idQuestion: String,
    val nameQuestion: String,
    val idAnswer : String,
    val nameAnswer: String,
    val img:String
){
    fun toView() = ReadyAnswer(id,idQuestion,nameQuestion,idAnswer,nameAnswer,img)
}