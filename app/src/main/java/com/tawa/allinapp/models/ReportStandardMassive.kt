package com.tawa.allinapp.models

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.remote.entities.SynReportStandardMassiveRemote

data class ReportStandardMassive(
    val idReport: String,
    val idPv: String,
    val idCompany : String,
    val report: List<ReportStandard>,
    val stateReport:String,
    val dateCreation:String,
    val longitude: String,
    val latitude: String,
    val longitudeSync: String,
    val latitudeSync: String,
    val dateSync: String,
){
    fun toRequestRemote() = SynReportStandardMassiveRemote.Request(idReport,idPv,idCompany,report.map { it.toRequestRemoteMassive() },stateReport,dateCreation,longitude,latitude,longitudeSync,latitudeSync,dateSync)
}