package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking


class RoutesRemote {
    data class RoutesUser(
        @SerializedName("_id") val id:String,
        @SerializedName("descripcion_ptv") val Pv: String?,
        @SerializedName("id_zona") val zone: Zone,
        @SerializedName("id_ptv_corp") val corp: IdPtvCorp,
    ){
        fun toView() = Routes(id,Pv?:"",zone.zoneName?:"",zone.zoneDistrict?:"",corp.dirCorpPv?:"",corp.nameCorpPv?:"",corp.latitude?:0.0,corp.longitude?:0.0)
    }

    data class TrackingUser(
        @SerializedName("punto_venta") val pv:Pv,
        @SerializedName("visitas") val visits: List<Visits?>,
        @SerializedName("tareas") val tasks: List<Tasks?>,
    ){
        fun toView() = Tracking(pv.id,pv.Pv,pv.zone.zoneName?:"",pv.zone.zoneDistrict,pv.corp.dirCorpPv,pv.corp.nameCorpPv,pv.corp.latitude,pv.corp.longitude,
            visits,tasks)
    }

    data class Pv(
        @SerializedName("_id") val id:String,
        @SerializedName("descripcion_ptv") val Pv: String,
        @SerializedName("id_zona") val zone: Zone,
        @SerializedName("id_ptv_corp") val corp: IdPtvCorp,
    )

    data class ListUser(
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_apellido") val name: String,
    )
    {
        fun toView() = RoutesUser(id,name)
    }

    data class Zone(
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_zona") val zoneName: String,
        @SerializedName("nombre_distrito") val zoneDistrict: String,
    )

    data class IdPtvCorp(
        @SerializedName("_id") val id: String,
        @SerializedName("direccion_pvt_corp") val dirCorpPv: String,
        @SerializedName("nombre_ptv_corp") val nameCorpPv: String,
        @SerializedName("latitud") val latitude: Double?,
        @SerializedName("longitud") val longitude: Double?,
    )

    data class Visits(
        @SerializedName("_id") val id: String,
        @SerializedName("latitud") val latitude: Double,
        @SerializedName("longitud") val longitude: Double,
        @SerializedName("fe_creacion") val creation: String,
        @SerializedName("comentario") val comment: String,
    )

    data class Tasks(
        @SerializedName("_id") val id: String,
        @SerializedName("latitud") val latitude: Double,
        @SerializedName("longitud") val longitude: Double,
        @SerializedName("status_reporte") val reportState:String,
    )



}