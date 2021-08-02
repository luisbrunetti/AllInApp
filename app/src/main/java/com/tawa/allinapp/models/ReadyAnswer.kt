package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.ReadyAnswerModel


data class ReadyAnswer(
    val id: Int,
    val idQuestion: String,
    val nameQuestion: String,
    val idAnswer : String,
    val nameAnswer: String,
    val img:String
){
    fun toModel() = ReadyAnswerModel(id,idQuestion,nameQuestion,idAnswer,nameAnswer,img)
}