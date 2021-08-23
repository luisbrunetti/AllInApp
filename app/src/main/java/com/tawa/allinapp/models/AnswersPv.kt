package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AnswersPvModel

data class AnswersPv(
    val id: Int,
    val idPv: String,
    val idAnswer: String,
    val idQuestion: String,
    val nameAnswer: String,
    val image :String,
    val idUser: String
)
{
    fun toModel() = AnswersPvModel(id,idPv,idAnswer,idQuestion,nameAnswer,image,idUser)
}