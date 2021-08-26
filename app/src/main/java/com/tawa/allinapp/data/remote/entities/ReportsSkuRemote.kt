package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Lines
import com.tawa.allinapp.models.ReportStatus
import java.util.*
import kotlin.collections.ArrayList

class ReportsSkuRemote {

    data class Request (
        @SerializedName( "id_reporte_pdv")
        val idReportePdv: String,

        @SerializedName("id_punto_venta")
        val idPuntoVenta: String,

        @SerializedName( "id_empresa")
        val idEmpresa: String,

        @SerializedName( "status_reporte")
        val stateReport: String,

        @SerializedName( "lineas_respuestas")
        val lineasRespuestas: List<LineasRespuesta>,

        @SerializedName( "longitud")
        val longitude: String,

        @SerializedName( "latitud")
        val latitude: String,

    )

    data class RequestMassive (
        @SerializedName( "id_reporte_pdv")
        val idReportePdv: String,

        @SerializedName("id_punto_venta")
        val idPuntoVenta: String,

        @SerializedName( "id_empresa")
        val idEmpresa: String,

        @SerializedName( "lineas_respuestas")
        val lineasRespuestas: List<LineasRespuesta>,

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

        )

    data class idUserAsig(
        @SerializedName( "_id")
        val id: String,

        @SerializedName( "_usuario")
        val user: String,

    )

    data class LineasRespuesta (
        @SerializedName("id_linea")
        val idLinea: String,

        @SerializedName("stock")
        val stock: Boolean,

        @SerializedName("exhibicion")
        val exhibicion: Boolean,

        @SerializedName("precio")
        val precio: Float,
        @SerializedName("observaciones")
        val observaciones: ArrayList<String>
    ){
        fun toView() = Lines(idLinea,stock,exhibicion,precio,observaciones)
    }

    data class Message(
        @SerializedName( "_id")
        val id: String,

    )

    data class Response(
        @SerializedName("id_user_modificador")
        val idUserModif: String,

        @SerializedName("fe_modificacion")
        val DateModif: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("id_empresa")
        val idCompany: IDEmpresa,

        @SerializedName( "id_user_asig")
        val idUserAsg: idUserAsig,


        @SerializedName("id_reporte_pdv")
        val idReportPdv: IdReportPdv,

    )


    data class IdReportPdv (
        @SerializedName("_id")
        val id: String,

        @SerializedName("numero_semana")
        val numeroSemana: Long,

        @SerializedName("id_punto_venta")
        val idPuntoVenta: IDPuntoVenta,

        @SerializedName("id_empresa")
        val idEmpresa: IDEmpresa,

        val lineas: List<Lineas>,

        @SerializedName("id_user_creador")
        val idUserCreador:String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long
    )

    data class IDEmpresa (
        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_empresa")
        val nombreEmpresa: String
    )


    data class IDPuntoVenta (
        @SerializedName("_id")
        val id: String,

        @SerializedName("descripcion_ptv")
        val descripcionPtv: String
    )


    data class Lineas (
        @SerializedName("_id")
        val id: String,

        @SerializedName("id_producto")
        val idProducto: IdProducto,

        val inventario: Int,
        val precio: Float
    )

    data class IdProducto (
        @SerializedName("id_user_modificador")
        val idUserModificador: Any? = null,

        @SerializedName("fe_modificacion")
        val feModificacion: Any? = null,

        val status: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_producto")
        val nombreProducto: String? = null,


        @SerializedName("id_subsegmentoProd")
        val idSubsegmentoProd: IdSubSegmentoProd? = null,

        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,


    )


    data class IdSubSegmentoProd(
        @SerializedName("id_user_modificador")
        val idUserModificador: Any? = null,

        @SerializedName("fe_modificacion")
        val feModificacion: Any? = null,

        val status: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_subsegmento")
        val nombreSubsegmento: String? = null,

        @SerializedName("id_segmentoProd")
        val idSegmentoProd: IdSegmentoProd? = null,

        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,

    )

    data class IdSegmentoProd(
        @SerializedName("id_user_modificador")
        val idUserModificador: Any? = null,

        @SerializedName("fe_modificacion")
        val feModificacion: Any? = null,

        val status: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_segmento")
        val nombreSegmento: String? = null,

        @SerializedName("id_subcategoriaProd")
        val idSubcategoriaProd: IdSubCategoriaProd? = null,

        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,

    )

    data class  IdSubCategoriaProd(
        @SerializedName("id_user_modificador")
        val idUserModificador: Any? = null,

        @SerializedName("fe_modificacion")
        val feModificacion: Any? = null,

        val status: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_subcategoria")
        val nombreSubcategoria: String? = null,


        @SerializedName("id_categoriaProd")
        val idCategoriaProd: IdCategoriaProd? = null,


        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,

    )

    data class IdCategoriaProd(
        @SerializedName("id_user_modificador")
        val idUserModificador: Any? = null,

        @SerializedName("fe_modificacion")
        val feModificacion: Any? = null,

        val status: String,

        @SerializedName("_id")
        val id: String,

        @SerializedName("nombre_categoria")
        val nombreCategoria: String? = null,

        @SerializedName("id_marcaProd")
        val idMarcaProd: String? = null,

        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,

    )


}