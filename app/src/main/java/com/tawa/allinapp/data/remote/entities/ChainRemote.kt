package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Chain
import com.tawa.allinapp.models.Channel

class ChainRemote {
    data class Response(
        @SerializedName("id_user_modificador") val userModify: String?,
        @SerializedName("fe_modificacion") val feModify: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_cadena") val description: String?,
        @SerializedName("id_canal") val channel: String?,
        @SerializedName("id_retail") val retail: String?,
        @SerializedName("ruta_img") val image: String?,
        @SerializedName("id_empresa") val idCompany: String?,
        @SerializedName("vers_ruta") val versionRoute: String?,
        @SerializedName("id_user_creador") val userCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        fun toModel() = Chain(id,status,description,idCompany)
    }
}