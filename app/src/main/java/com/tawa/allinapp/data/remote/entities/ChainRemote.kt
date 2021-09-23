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
        @SerializedName("id_canal") val channel: Channel?,
        @SerializedName("id_retail") val retail: Retail?,
        @SerializedName("ruta_img") val image: String?,
        @SerializedName("id_empresa") val idCompany: String?,
        @SerializedName("vers_ruta") val versionRoute: String?,
        @SerializedName("id_user_creador") val userCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        fun toModel() = Chain(id,status,description,idCompany)
    }
    data class Channel(
    @SerializedName("fe_creacion")
    val feCreacion: String, // 2021-07-23T00:00:00.000Z
    @SerializedName("fe_modificacion")
    val feModificacion: Any, // null
    @SerializedName("_id")
    val id: String, // 60fb15788b978f0ad5e4ac7e
    @SerializedName("id_empresa")
    val idEmpresa: String, // 60fb04218b978f3740e4ac26
    @SerializedName("id_user_creador")
    val idUserCreador: String, // 60fb02fa417fdc43ec350b61
    @SerializedName("id_user_modificador")
    val idUserModificador: Any, // null
    @SerializedName("nombre_canal")
    val nombreCanal: String, // MODERNO
    @SerializedName("status")
    val status: String, // ACT
    @SerializedName("__v")
    val v: Int // 0
    )
    data class Retail(
        @SerializedName("fe_creacion")
        val feCreacion: String, // 2021-07-23T00:00:00.000Z
        @SerializedName("fe_modificacion")
        val feModificacion: Any, // null
        @SerializedName("_id")
        val id: String, // 60fb15c88b978f44b7e4ac8a
        @SerializedName("id_empresa")
        val idEmpresa: String, // 60fb04218b978f3740e4ac26
        @SerializedName("id_user_creador")
        val idUserCreador: String, // 60fb02fa417fdc43ec350b61
        @SerializedName("id_user_modificador")
        val idUserModificador: Any, // null
        @SerializedName("nombre_retail")
        val nombreRetail: String, // SUPERMERCADO
        @SerializedName("status")
        val status: String, // ACT
        @SerializedName("__v")
        val v: Int // 0
    )

}