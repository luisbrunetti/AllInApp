package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question

class QuestionsRemote {
    data class Response(
        @SerializedName("pregunta") val question: ResponseQuestion,
        @SerializedName("respuestas") val answers: List<ResponseAnswers>,

    )

    data class ResponseQuestion(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_pregunta") val questionName: String,
        @SerializedName("numero_orden") val order : Int,
        @SerializedName("id_reporte") val idReport: String,
        @SerializedName("objeto") val objectType: String,
        @SerializedName("id_user_creador") val idUserCreator: String,
        @SerializedName("id_user_modificador") val idUserModifier: String?,
        @SerializedName("fe_modificacion") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,
        )
    {
          fun toView() = Question(id,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation)
          fun toModel() = QuestionModel(id,questionName,order,idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation)
    }

    data class ResponseAnswers(
        @SerializedName("_id") val id: String,
        @SerializedName("status") val status: String,
        @SerializedName("nombre_respuesta") val answerName: String,
        @SerializedName("numero_orden") val order : Int,
        @SerializedName("id_pregunta") val idQuestion: String,
        @SerializedName("id_user_creador") val idUserCreator: String,
        @SerializedName("id_user_modificador") val idUserModifier: String?,
        @SerializedName("fe_modificacion") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,
        @SerializedName("fila") val row: Boolean,
        @SerializedName("columna") val column:Boolean,

        ){
        fun toView() = Answer(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation,"VACIO","","",row,column)
        fun toModel() = AnswerModel(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation,"VACIO","","",row,column)
    }

}