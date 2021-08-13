package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.ReportPvModel

data class ReportPv(
    val id: Int,
    val idPv: String,
    val idReport: String,
    val state: String,
    val type:String
)
{
    fun toModel() = ReportPvModel(id,idPv,idReport,state,type)
}