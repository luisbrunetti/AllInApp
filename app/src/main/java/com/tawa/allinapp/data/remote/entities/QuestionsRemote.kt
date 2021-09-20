package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.AnswerModel
import com.tawa.allinapp.data.local.models.QuestionModel
import com.tawa.allinapp.data.local.models.SubAnswerModel
import com.tawa.allinapp.models.Answer
import com.tawa.allinapp.models.Question
import com.tawa.allinapp.models.SubAnswer

class QuestionsRemote {
    data class Response(
        @SerializedName("pregunta") val question: ResponseQuestion,
        @SerializedName("respuestas") val answers: List<ResponseAnswers>,
        @SerializedName("subrespuestas") val subAnswers: List<ResponseSubAnswers?>,

    )

    data class ResponseQuestion(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_pregunta") val questionName: String,
        @SerializedName("numero_orden") val order : Float,
        @SerializedName("id_reporte") val idReport: String,
        @SerializedName("objeto") val objectType: String,
        @SerializedName("id_user_creador") val idUserCreator: String,
        @SerializedName("id_user_modificador") val idUserModifier: String?,
        @SerializedName("fe_modificacion") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,
        @SerializedName("obligatorio") val required: Boolean,
        )
    {
          fun toView() = Question(id,questionName,order.toInt(),idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation,required)
          fun toModel() = QuestionModel(id,questionName,order.toInt(),idReport,objectType,idUserCreator,idUserModifier,dateModify,dateCreation,required)
    }

    data class ResponseAnswers(
        @SerializedName("_id") val id: String,
        @SerializedName("status") val status: String,
        @SerializedName("nombre_respuesta") val answerName: String,
        @SerializedName("numero_orden") val order : String,
        @SerializedName("id_pregunta") val idQuestion: String,
        @SerializedName("id_user_creador") val idUserCreator: String,
        @SerializedName("id_user_modificador") val idUserModifier: String?,
        @SerializedName("fe_modificacion") val dateModify: String?,
        @SerializedName("fe_creacion") val dateCreation: String,
        @SerializedName("fila") val row: Boolean,
        @SerializedName("columna") val column:Boolean,

        ){
        fun toView() = Answer(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation,"VACIO","","",row,column,"",false)
        fun toModel() = AnswerModel(id,status,answerName,order,idQuestion,idUserCreator,idUserModifier,dateModify,dateCreation,"VACIO","","",row,column,"",false)
    }

    data class ResponseSubAnswers(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_subrespuesta") val subAnswerName: String,
        @SerializedName("numero_orden") val order : String,
        @SerializedName("id_pregunta") val idQuestion: String,
    ){
        fun toView() = SubAnswer(id,subAnswerName,order,idQuestion)
        fun toModel() =SubAnswerModel(id,subAnswerName,order,idQuestion)
    }


}