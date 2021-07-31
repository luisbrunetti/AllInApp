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

        @SerializedName( "lineas_respuestas")
        val lineasRespuestas: List<LineasRespuesta>
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

    data class Response (
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