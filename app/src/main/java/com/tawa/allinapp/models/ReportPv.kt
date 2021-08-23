package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.ReportPvModel

data class ReportPv(
    val id: Int,
    val idPv: String,
    val idReport: String,
    val idUser:String,
    val state: String,
    val type:String,
    val time:String,
    val latitude:String,
    val longitude:String
)
{
    fun toModel() = ReportPvModel(id,idPv,idReport,idUser,state,type,time,latitude,longitude)
}