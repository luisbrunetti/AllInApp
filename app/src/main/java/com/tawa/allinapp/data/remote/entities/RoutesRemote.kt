package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Routes
import com.tawa.allinapp.models.RoutesUser
import com.tawa.allinapp.models.Tracking

    data class VisitsCount(
        @SerializedName("titulo") val title: String,
        @SerializedName("total") val total: String,
        @SerializedName("concluidas") val complete: String,
        @SerializedName("pendientes") val pending: String,
    )

    data class TasksCount(
        @SerializedName("titulo") val title: String,
        @SerializedName("totalTareasHacer") val total: String,
        @SerializedName("tareasCompletadas") val complete: String,
        @SerializedName("tareasPendientes") val pending:String,
    )


class RoutesRemote {
    data class RoutesUser(
        @SerializedName("_id") val id:String,
        @SerializedName("descripcion_ptv") val Pv: String?,
        @SerializedName("id_zona") val zone: Zone,
        @SerializedName("id_ptv_corp") val corp: IdPtvCorp,
        @SerializedName("visitas") val visits: VisitsCount,
        @SerializedName("reportes") val tasks: TasksCount,
    ){
        fun toView() = Routes(id,Pv?:"",zone.zoneName?:"",zone.zoneDistrict?:"",corp.dirCorpPv?:"",corp.nameCorpPv?:"",corp.latitude?:0.0,corp.longitude?:0.0,visits.total,visits.complete,visits.pending,tasks.total,tasks.complete,tasks.pending)
    }

    data class TrackingUser(
        @SerializedName("punto_venta") val pv:Pv,
        @SerializedName("visitas") val visits: List<Visits?>,
        @SerializedName("tareas") val tasks: List<Tasks?>,
        @SerializedName("reportes") val reports: Reports,
        @SerializedName("checks") val checks: Checks,
    ){
        fun toView() = Tracking(pv.id, pv.Pv, pv.zone.zoneName?:"", pv.zone.zoneDistrict?:"", pv.corp.dirCorpPv, pv.corp.nameCorpPv, pv.corp.latitude, pv.corp.longitude, pv.corp.codPvCrop ?: "", visits, tasks,checks,reports)

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
        @SerializedName("codigo_ptv_corp") val codPvCrop: String
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
        @SerializedName("fe_creacion") val creation:String,
    )

    data class Checks(
        @SerializedName("checkIn")
        val checkIn: CheckIn,
        @SerializedName("checkOut")
        val checkOut: CheckOut,
        @SerializedName("titulo")
        val titulo: String // VISITAS
    )

    data class CheckIn(
        @SerializedName("concluidas")
        val concluidas: Int, // 0
        @SerializedName("pendientes")
        val pendientes: Int, // 1
        @SerializedName("total")
        val total: Int // 1
    )

    data class CheckOut(
        @SerializedName("concluidas")
        val concluidas: Int, // 0
        @SerializedName("pendientes")
        val pendientes: Int, // 1
        @SerializedName("total")
        val total: Int // 1
    )

    data class Reports(
        @SerializedName("tareasCompletadas") val tareasCompletadas: Int, // 0
        @SerializedName("tareasPendientes") val tareasPendientes: Int, // 1
        @SerializedName("titulo") val titulo: String, // TAREAS
        @SerializedName("totalTareasHacer") val totalTareasHacer: Int // 1
    )


}