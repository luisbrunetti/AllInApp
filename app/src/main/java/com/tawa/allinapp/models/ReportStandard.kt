package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.SynReportStandardRemote

data class ReportStandard(
    val idQuestion: String,
    val nameQuestion: String,
    val answers : List<AnswerStandard>,
){
    fun toRequestRemote() = SynReportStandardRemote.Report(idQuestion,nameQuestion,answers.map { it.toRequestRemote() })
}