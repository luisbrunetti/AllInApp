package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.ReportStatus

class ReportsStatusRemote {
    data class Response(
        @SerializedName("status") val status: String? =null,
        @SerializedName("fe_conexion") val lastConnection: String? =null,
        @SerializedName("ul_latitud") val lastLatitude: Double? =null,
        @SerializedName("ul_longitud") val lastLongitude: Double? =null,
        @SerializedName("bateria") val battery: Int? =null,
        @SerializedName("nombre_apellido") val name:String? =null,

        ){
        fun toView() = ReportStatus(status,lastConnection,lastLatitude,lastLongitude,battery,name)
    }
}