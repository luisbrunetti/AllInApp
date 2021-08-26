package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Report

class ReportsRemote {
    data class Response(
        @SerializedName("ids_reporte_estandar") val reports:List<ReportType>,
        @SerializedName("id_empresa") val idCompany: Company,
        @SerializedName("id_user_modificador") val idUserMod: String?,
        @SerializedName("fe_modificacion") val feMod: String?,
        @SerializedName("id_user_asig") val userAsig: UserAsig,


    ){
     //   fun toModel() = ReportModel(reports.id?:"",reports.reportName?:"",idCompany.id?:"",idCompany.nameCompany?:"",idUser?:"",idUserMod?:"",feMod?:"",feCreate?:"","No iniciado")
    }

    data class ReportType(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_reporte") val reportName: String?,
    )

    data class  Company(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_empresa") val nameCompany: String?,

    )

    data class  UserAsig(
        @SerializedName("_id") val id: String?,
       // @SerializedName("nombre_empresa") val nameCompany: String?,

        )
}