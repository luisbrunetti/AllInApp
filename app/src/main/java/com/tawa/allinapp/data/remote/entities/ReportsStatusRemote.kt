package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.ReportStatus

class ReportsStatusRemote {
    data class Response(
        @SerializedName("status") val status: String,
        @SerializedName("fe_conexion") val lastConnection: String,
        @SerializedName("ul_latitud") val lastLatitude: Double,
        @SerializedName("ul_longitud") val lastLongitude: Double,
        @SerializedName("bateria") val battery: Int,
        @SerializedName("nombre_apellido") val name:String,

        ){
        fun toView() = ReportStatus(status,lastConnection,lastLatitude,lastLongitude,battery,name)
    }
}