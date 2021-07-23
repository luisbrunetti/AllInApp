package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.models.Company

class CheckRemote {
    data class Request(
        @SerializedName("id_cronograma") val schedule: String?,
        @SerializedName("latitud") val latitude: Double?,
        @SerializedName("longitud") val longitude: Double?,
    )

    data class Response(
        @SerializedName("_id") val id: String?,
        @SerializedName("id_cronograma") val idCronograma: String?,
        @SerializedName("latitud") val latitud: Double?,
        @SerializedName("longitud") val longitud: Double?,
        @SerializedName("id_empresa") val company: String?,
        @SerializedName("id_punto_venta") val pv: String?,
        @SerializedName("id_user_creador") val idUserCreate: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
        @SerializedName("comentario") val comment: String?,
    )

}