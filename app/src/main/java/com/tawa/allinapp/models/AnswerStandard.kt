package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.SynReportStandardMassiveRemote
import com.tawa.allinapp.data.remote.entities.SynReportStandardRemote


data class AnswerStandard(
    val idAnswer: String,
    val answerName: String,
    val input:String
){
    fun toRequestRemote() = SynReportStandardRemote.Respuesta(idAnswer,answerName,input)
    fun toRequestRemoteMassive() = SynReportStandardMassiveRemote.Respuesta(idAnswer,answerName,input)
}