package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.ReportPv
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser

class RoutesRemote {
    data class Response(
        @SerializedName("_id") val id:String,
        @SerializedName("descripcion_ptv") val Pv: String?,
        @SerializedName("id_zona") val zone: Zone,
        @SerializedName("id_ptv_corp") val corp: IdPtvCorp,
    ){
        fun toModel() = Routes(id,Pv?:"",zone.zoneName?:"",zone.zoneDistrict?:"",corp.dirCorpPv?:"",corp.nameCorpPv?:"",corp.latitude?:0.0,corp.longitude?:0.0)
    }

    data class ListUser(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_apellido") val name: String,
    )
    {
        fun toView() = RoutesUser(id,name)
    }

    data class Zone(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_zona") val zoneName: String?,
        @SerializedName("nombre_distrito") val zoneDistrict: String?,
    )

    data class IdPtvCorp(
        @SerializedName("_id") val id: String?,
        @SerializedName("direccion_pvt_corp") val dirCorpPv: String?,
        @SerializedName("nombre_ptv_corp") val nameCorpPv: String?,
        @SerializedName("latitud") val latitude: Double?,
        @SerializedName("longitud") val longitude: Double?,
    )

}