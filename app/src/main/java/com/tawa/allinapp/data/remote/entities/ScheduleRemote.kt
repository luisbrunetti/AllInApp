package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.ScheduleModel

class ScheduleRemote {
    data class Response(
        @SerializedName("id_user_modificador") val idUserModify: Any? = null,
        @SerializedName("fe_modificacion") val feModify: Any? = null,
        @SerializedName("_id") val id: String?,
        @SerializedName("fe_inicio") val feStart: String?,
        @SerializedName("fe_final") val feEnd: String?,
        @SerializedName("id_user_asig") val userAssigned: User,
        @SerializedName("observacion") val observation: String?,
        @SerializedName("id_punto_venta") val pv: PV?,
        @SerializedName("id_empresa") val company: Company?,
        @SerializedName("id_user_creador") val idUserCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        //TODO change chekin latitude and longitude
        //fun toModel() = ScheduleModel(id?:"",pv?.description,pv?.zone?.id,"",company?.id,pv?.zone?.latitude,pv?.zone?.longitude)
        fun toModel() = ScheduleModel(id?:"",pv?.description,pv?.zone?.id,"",company?.id,-12.069630,-77.060643)
    }

    data class Company (
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_empresa") val name: String
    )
    data class User (
        @SerializedName("_id") val id: String?,
        @SerializedName("usuario") val user: String?,
        @SerializedName("nombre_apellido") val name: String
    )
    data class PV (
        @SerializedName("id_user_modificador") val idUserModify: Any? = null,
        @SerializedName("fe_modificacion") val feModify: Any? = null,
        @SerializedName("status") val status: String?,
        @SerializedName("_id") val id: String?,
        @SerializedName("descripcion_ptv") val description: String?,
        @SerializedName("direccion_ptv") val direction: String?,
        @SerializedName("id_zona") val zone: Zone?,
        @SerializedName("nombre_jefe") val bossName: String?,
        @SerializedName("tlf_jefe") val bossPhone: String?,
        @SerializedName("id_cadena") val chain: String?,
        @SerializedName("id_empresa") val company: String?,
        @SerializedName("id_ptv_corp") val corp: String?,
        @SerializedName("id_user_creador") val idUserCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    )
    data class Zone(
        @SerializedName("id_user_modificador") val idUserModify: Any? = null,
        @SerializedName("fe_creacion") val feCreated: String?,
        @SerializedName("fe_modificacion") val feModify: Any? = null,
        @SerializedName("status") val status: String?,
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_zona") val name: String?,
        @SerializedName("nombre_distrito") val district: String?,
        @SerializedName("latitud") val latitude: Double,
        @SerializedName("longitud") val longitude: Double,
        @SerializedName("id_empresa") val company: String?,
        @SerializedName("id_user_creador") val idUserCreator: String?,
    )

}

