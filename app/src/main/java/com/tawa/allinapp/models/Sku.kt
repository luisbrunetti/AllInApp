package com.tawa.allinapp.models


import com.tawa.allinapp.data.local.models.SkuModel

data class Sku(
    val id:String ,
    val idPv :String,
    val idCompany:String,
    val idUser:String,
    val state : String,
    val type:String,
    val dateCreation:String,
    val latitude:String,
    val longitude:String
){
    fun toModel() = SkuModel(id,idPv,idCompany,idUser,state,type,dateCreation,latitude,longitude)
}