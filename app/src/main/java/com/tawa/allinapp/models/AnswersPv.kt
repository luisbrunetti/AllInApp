package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.AnswersPvModel
import com.tawa.allinapp.data.local.models.ReportPvModel

data class AnswersPv(
    val id: Int,
    val idPv: String,
    val idAnswer: String,
    val idQuestion: String,
    val nameAnswer: String,
    val image :String
)
{
    fun toModel() = AnswersPvModel(id,idPv,idAnswer,idQuestion,nameAnswer,image)
}