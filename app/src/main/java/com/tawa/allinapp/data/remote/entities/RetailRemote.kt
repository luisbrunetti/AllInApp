package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Retail

class RetailRemote {
    data class Response(
        @SerializedName("id_user_modificador") val userModify: String?,
        @SerializedName("fe_modificacion") val feModify: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_retail") val description: String?,
        @SerializedName("id_empresa") val idCompany: String?,
        @SerializedName("id_user_creador") val userCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        fun toModel() = Retail(id,status,description,idCompany)
    }
}