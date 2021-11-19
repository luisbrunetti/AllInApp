package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.PdvModel
import com.tawa.allinapp.models.Pdv


class PdvRemote {

    data class Request(
        @SerializedName("campos_solicitud") val fields:Fields,
    )

    data class Fields(
        @SerializedName("nombre_jefe") val nameUser:String?,
        @SerializedName("tlf_jefe") val phoneUser: String?,
        @SerializedName("ruc") val ruc: String?,
        @SerializedName("latitud") val latitude: String?,
        @SerializedName("longitud") val longitude: String?,
        @SerializedName("imagen") val image: String?,
        @SerializedName("id_user") val idUser: String?,
    )

    data class Response(
        @SerializedName("_id") val id:String,
        @SerializedName("id_user") var idUser:String?,
        @SerializedName("imagen") val image: String?,
        @SerializedName("ruc") val ruc: String?,
        @SerializedName("descripcion_ptv") val pdvDescription: String?,
        @SerializedName("nombre_jefe") val nameUser: String?,
        @SerializedName("tlf_jefe") val phoneUser: String?,
        @SerializedName("id_ptv_corp") val corp: IdPtvCorp,
    ){
        //fun toView() = Pdv(id,idUser ?:"",image?:"",ruc?:"",corp.nameCorpPv,nameUser?:"",phoneUser?:"",corp.latitude?:0.0,corp.longitude?:0.0,"")
        //fun toModel() = PdvModel(id,idUser ?: "",image?:"",ruc?:"",corp.nameCorpPv,nameUser?:"",phoneUser?:"",corp.latitude?:0.0,corp.longitude?:0.0,"")
        fun toView() = Pdv(id,idUser ?:"", corp.images[0]?.imagen ?: "",ruc?:"",corp.nameCorpPv,nameUser?:"",phoneUser?:"",corp.latitude?:0.0,corp.longitude?:0.0,"")
        fun toModel() = PdvModel(id,idUser ?: "",corp.images[0]?.imagen ?: "",ruc?:"",corp.nameCorpPv,nameUser?:"",phoneUser?:"",corp.latitude?:0.0,corp.longitude?:0.0,"")
    }
    data class IdPtvCorp(
        @SerializedName("_id") val id: String,
        @SerializedName("direccion_pvt_corp") val dirCorpPv: String,
        @SerializedName("nombre_ptv_corp") val nameCorpPv: String,
        @SerializedName("latitud") val latitude: Double?,
        @SerializedName("longitud") val longitude: Double?,
        @SerializedName("imagenes") val images: List<Image?>,
    )

    data class Image(
        @SerializedName("descripcion") val descripcion: String,
        //@SerializedName("_id") val id: String,
        @SerializedName("imagen") val imagen: String?
    )

}