package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question

class QuestionsRemote {
    data class Response(
        @SerializedName("_id") val id: String,
        @SerializedName("status") val status: String,
        @SerializedName("nombre_preg") val questionName: String,
        @SerializedName("nu_orden") val order : Int,
        @SerializedName("id_reporte") val idReport: String,
        @SerializedName("objeto") val objectType: String,
        @SerializedName("id_usua_cread") val idUserCreator: String,
        @SerializedName("id_usua_modi") val idUserModifier: String?,
        @SerializedName("fe_modi") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,
        @SerializedName("respuestas") val answers: List<ResponseAnswers>,


    ){
        fun toView() = Question(id,status,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation)
        fun toModel() = QuestionModel(id,status,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation)
    }

    data class ResponseAnswers(
        @SerializedName("_id") val id: String,
        @SerializedName("status") val status: String,
        @SerializedName("nombre_respt") val answerName: String,
        @SerializedName("nu_orden") val order : Int,
        @SerializedName("id_pregunta") val idQuestion: String,
        @SerializedName("id_usua_cread") val idUserCreator: String,
        @SerializedName("id_usua_modi") val idUserModifier: String?,
        @SerializedName("fe_modi") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,


        ){
        fun toView() = Answer(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation)
        fun toModel() = AnswerModel(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation)
    }

}