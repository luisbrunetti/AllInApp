package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.ReportStatus
import java.util.*

class ReportsSkuRemote {
    data class Response (
        @SerializedName("_id")
        val id: String,

        @SerializedName("numero_semana")
        val numeroSemana: Long,

        @SerializedName("id_punto_venta")
        val idPuntoVenta: IDPuntoVenta,

        @SerializedName("id_empresa")
        val idEmpresa: IDEmpresa,

        val lineas: List<Linea>,

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


    data class Linea (
        @SerializedName("_id")
        val id: String,

        @SerializedName("id_producto")
        val idProducto: ID,

        val inventario: Long,
        val precio: Long
    )

    data class ID (
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
        val idSubsegmentoProd: ID? = null,

        @SerializedName("id_empresa")
        val idEmpresa: String,

        @SerializedName("id_user_creador")
        val idUserCreador: String,

        @SerializedName("fe_creacion")
        val feCreacion: String,

        @SerializedName("__v")
        val v: Long,

        @SerializedName("nombre_subsegmento")
        val nombreSubsegmento: String? = null,

        @SerializedName("id_segmentoProd")
        val idSegmentoProd: ID? = null,

        @SerializedName("nombre_segmento")
        val nombreSegmento: String? = null,

        @SerializedName("id_subcategoriaProd")
        val idSubcategoriaProd: ID? = null,

        @SerializedName("nombre_subcategoria")
        val nombreSubcategoria: String? = null,

        @SerializedName("id_categoriaProd")
        val idCategoriaProd: ID? = null,

        @SerializedName("nombre_categoria")
        val nombreCategoria: String? = null,

        @SerializedName("id_marcaProd")
        val idMarcaProd: String? = null
    )
}