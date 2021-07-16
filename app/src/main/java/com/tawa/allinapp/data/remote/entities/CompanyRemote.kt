package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.models.Company

class CompanyRemote {
    data class Response(
        @SerializedName("_id") val id: String,
        @SerializedName("numero_ruc") val ruc: String?,
        @SerializedName("nombre_empresa") val name: String?,
        @SerializedName("giro_empresa") val description: String?,
    ){
        fun toView() = Company(id,ruc?:"",name?:"",description?:"")
        fun toModel() = CompanyModel(id,ruc?:"",name?:"",description?:"")
    }
}