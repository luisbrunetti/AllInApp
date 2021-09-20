package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.User

class UserRemote {
    data class Response(
        @SerializedName("roles") val roles: List<String>,
        @SerializedName("empresas") val companies: List<CompanyRemote>,
        @SerializedName("latitud") val latitude: String?,
        @SerializedName("longitud") val longitude: String?,
        @SerializedName("fe_modificacion") val feModify: String?,
        @SerializedName("id_user_modificador") val idUserModify: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("fe_conexion") val connection: String?,
        @SerializedName("ul_latitud") val ulLatitude: Double,
        @SerializedName("ul_longitud") val ulLongitude: Double,
        @SerializedName("bateria") val battery: Double,
        @SerializedName("_id") val id: String,
        @SerializedName("usuario") val user: String?,
        @SerializedName("nombre_apellido") val fullName: String?,
        @SerializedName("contraseña") val password: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        fun toModel() = User(roles,companies.map { it.toModel(id) },latitude,longitude,feModify,idUserModify,status?:"",connection?:"",ulLatitude,ulLongitude,battery,id,user?:"",fullName?:"",password?:"",email?:"",feCreated?:"")
    }

    data class CompanyRemote (
        @SerializedName("_id") val id: String,
        @SerializedName("nombre_empresa") val companyName: String
    ){
        fun toModel(idUser:String) = Company(id,"", companyName, "",idUser,"")
    }
}