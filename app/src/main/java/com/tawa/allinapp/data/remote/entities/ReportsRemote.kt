package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Report

class ReportsRemote {
    data class Response(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_rpte") val report: String,
        @SerializedName("id_empr") val idCompany: String,
        @SerializedName("id_usua_cread") val idUser: String,
    ){
        fun toView() = Report(id,report,idCompany,idUser)
        fun toModel() = ReportModel(id,report,idCompany,idUser)
    }
}