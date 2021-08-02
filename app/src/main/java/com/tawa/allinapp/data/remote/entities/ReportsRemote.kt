package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Report

class ReportsRemote {
    data class Response(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_rpte") val reportName: String?,
        @SerializedName("id_empr") val idCompany: String?,
        @SerializedName("id_usua_cread") val idUser: String?,
        @SerializedName("id_usua_modi") val idUserMod: String?,
        @SerializedName("fe_modi") val feMod: String?,
        @SerializedName("fe_creacion") val feCreate: String?,

    ){
        fun toModel() = ReportModel(id?:"",reportName?:"",idCompany?:"",idUser?:"",idUserMod?:"",feMod?:"",feCreate?:"","No iniciado")
    }
}