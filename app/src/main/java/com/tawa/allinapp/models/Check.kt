package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.CheckModel

data class Check(
    val id: Int,
    val schedule: String,
    val company: String,
    val pv: String,
    val idUser: String,
    val fechaRegistro: String,
    val latitud: String,
    val longitud: String,
    val comment: String,
    val state:String
){
    fun toModel() = CheckModel(id,schedule,company,pv,idUser,fechaRegistro,latitud,longitud,comment,state)
}