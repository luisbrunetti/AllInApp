package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Report

class ReportsRemote {
    data class Response(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_reporte") val reportName: String?,
        @SerializedName("id_empresa") val idCompany: Company,
        @SerializedName("id_user_creador") val idUser: String?,
        @SerializedName("id_user_modificador") val idUserMod: String?,
        @SerializedName("fe_modificacion") val feMod: String?,
        @SerializedName("fe_creacion") val feCreate: String?,

    ){
        fun toModel() = ReportModel(id?:"",reportName?:"",idCompany.id?:"",idCompany.nameCompany?:"",idUser?:"",idUserMod?:"",feMod?:"",feCreate?:"","No iniciado")
    }

    data class  Company(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_empresa") val nameCompany: String?,

    )
}