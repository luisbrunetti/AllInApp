package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.ScheduleModel

class ScheduleRemote {
    data class Response(
        @SerializedName("id_user_modificador") val idUserModify: Any? = null,
        @SerializedName("fe_modificacion") val feModify: Any? = null,
        @SerializedName("status") val status: String,
        @SerializedName("_id") val id: String,
        @SerializedName("fe_inicio") val feStart: String,
        @SerializedName("fe_final") val feEnd: String,
        @SerializedName("id_user_asig") val userAssigned: User,
        @SerializedName("observacion") val observation: String,
        @SerializedName("id_punto_venta") val pv: PV,
        @SerializedName("id_empresa") val company: Company,
        @SerializedName("id_user_creador") val idUserCreator: String,
        @SerializedName("fe_creacion") val feCreated: String,
        @SerializedName("__v") val v: Long
    ){
        fun toModel() = ScheduleModel(id?:"",pv.name,"","",company.id,"-12.069630","-77.060643")
    }

    data class Company (
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_empresa") val name: String
    )
    data class PV (
        @SerializedName("_id") val id: String,
        @SerializedName("descripcion_ptv") val name: String
    )
    data class User (
        @SerializedName("_id") val id: String,
        @SerializedName("usuario") val user: String,
        @SerializedName("nombre_apellido") val name: String
    )

}