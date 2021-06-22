package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.PVModel

class PVRemote {
    data class Response(
        @SerializedName("_id") val id: String?,
        @SerializedName("DE_PUNT_VENT") val description: String?,
        @SerializedName("ID_ZONA") val zone: String?,
        @SerializedName("CO_UBIC_GEOG") val codGeo: String?,
        @SerializedName("ID_EMPR") val idCompany: String?,
        @SerializedName("LAT") val latitude: String?,
        @SerializedName("LONG") val longitude: String?,
    ){
        fun toModel() = PVModel(id?:"",description,zone,codGeo,idCompany,latitude,longitude)
    }
}