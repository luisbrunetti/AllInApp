package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName

class ReportsPhotoRemote {
    data class Request(
        @SerializedName("id_empresa") val company: String?,
        @SerializedName("id_punto_venta") val pv: String?,
        @SerializedName("foto_a1") val fa1: String?,
        @SerializedName("foto_a2") val fa2: String?,
        @SerializedName("foto_a3") val fa3: String?,
        @SerializedName("foto_a4") val fa4: String?,
        @SerializedName("foto_a5") val fa5: String?,
        @SerializedName("foto_d1") val fd1: String?,
        @SerializedName("foto_d2") val fd2: String?,
        @SerializedName("foto_d3") val fd3: String?,
        @SerializedName("foto_d4") val fd4: String?,
        @SerializedName("foto_d5") val fd5: String?,
        @SerializedName("comentario") val comment: String?,
        @SerializedName("fe_creacion") val createAt: String?,
    )

    data class Response(
        @SerializedName("id_empresa") val company: String?,
        @SerializedName("id_punto_venta") val pv: String?,
        @SerializedName("foto_a1") val fa1: String?,
        @SerializedName("foto_a2") val fa2: String?,
        @SerializedName("foto_a3") val fa3: String?,
        @SerializedName("foto_a4") val fa4: String?,
        @SerializedName("foto_a5") val fa5: String?,
        @SerializedName("foto_d1") val fd1: String?,
        @SerializedName("foto_d2") val fd2: String?,
        @SerializedName("foto_d3") val fd3: String?,
        @SerializedName("foto_d4") val fd4: String?,
        @SerializedName("foto_d5") val fd5: String?,
        @SerializedName("comentario") val comment: String?,
        @SerializedName("fe_creacion") val createAt: String?,
    )
}