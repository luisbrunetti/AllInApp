package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.features.auth.Company

class CompanyRemote {
    data class Response(
        @SerializedName("_id") val id: String,
        @SerializedName("CO_EMPR") val code: String,
        @SerializedName("NU_RUCS") val ruc: String,
        @SerializedName("DE_NOMB") val name: String,
        @SerializedName("DE_GIRO") val description: String,
    ){
        fun toView() = Company(id,code,ruc,name,description)
        fun toModel() = CompanyModel(id,code,ruc,name,description)
    }
}