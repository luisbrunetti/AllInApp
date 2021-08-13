package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.models.Channel

class ChannelRemote {
    data class Response(
        @SerializedName("id_user_modificador") val userModify: String?,
        @SerializedName("fe_modificacion") val feModify: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("_id") val id: String?,
        @SerializedName("nombre_canal") val description: Double?,
        @SerializedName("id_empresa") val idCompany: Double?,
        @SerializedName("id_user_creador") val userCreator: String?,
        @SerializedName("fe_creacion") val feCreated: String?,
    ){
        fun toModel() = Channel(id,status,description,idCompany)
    }
}
