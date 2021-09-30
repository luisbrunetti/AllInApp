package com.tawa.allinapp.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.tawa.allinapp.data.local.models.CompanyModel
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Notify

class NotifyRemote {
    data class Response(
        @SerializedName("_id") val id: String?,
        @SerializedName("id_user") val idUser: String?,
        @SerializedName("mensaje") val message: String?,
        @SerializedName("fe_creacion") val dateCreation: String?,
    ){
        fun toView(user:String) = Notify(id,idUser,message,dateCreation,user)
    }

}