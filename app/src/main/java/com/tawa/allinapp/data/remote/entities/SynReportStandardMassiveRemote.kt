package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.data.local.models.ReportModel
import com.tawa.allinapp.models.*

class SynReportStandardMassiveRemote {
    data class Request (
        @SerializedName("id_reporte")
        val idReporte: String,

        @SerializedName("id_punto_venta")
        val idPuntoVenta: String,

        @SerializedName( "id_empresa")
        val idEmpresa: String,

        @SerializedName( "reporte")
        val reporte: List<Report>,

        @SerializedName( "status_reporte")
        val stateReport: String,

        @SerializedName( "fe_creacion")
        val dateCreation: String,

        @SerializedName( "longitud")
        val longitude: String,

        @SerializedName( "latitud")
        val latitude: String,

        @SerializedName( "longitud_sincronizacion")
        val longitudeSync: String,

        @SerializedName( "latitud_sincronizacion")
        val latitudeSync: String,

        @SerializedName( "fe_sincronizacion")
        val dateSync: String,

    ){
        fun toView() = ReportStandardMassive(idReporte,idPuntoVenta,idEmpresa,reporte.map { it.toView() },stateReport,dateCreation,longitude,latitude,longitudeSync,latitudeSync,dateSync)
    }

    data class Message(
        @SerializedName( "id_reporte")
        val idReport: String,
    )

    data class Report(
        @SerializedName("id_pregunta")
        val idPregunta: String,

        @SerializedName( "nombre_preg")
        val nombrePreg: String,

        val respuestas: List<Respuesta>
    )
    {
        fun toView()  = ReportStandard(idPregunta,nombrePreg,respuestas.map { it.toView() })
    }

    data class Respuesta (
        @SerializedName( "id_respuesta")
        val idRespuesta: String,

        @SerializedName( "nombre_respuesta")
        val nombreRespuesta: String,
        @SerializedName( "input")
        val input: String
    ){
        fun toView() = AnswerStandard(idRespuesta,nombreRespuesta,input)
    }

}