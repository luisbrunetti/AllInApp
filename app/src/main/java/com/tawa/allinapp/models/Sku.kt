package com.tawa.allinapp.models


import com.tawa.allinapp.data.local.models.SkuModel

data class Sku(
    val id:String ,
    val idPv :String,
    val idCompany:String,
    val state : String
){
    fun toModel() = SkuModel(id,idPv,idCompany,state)
}