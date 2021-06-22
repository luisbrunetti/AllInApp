package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.CheckModel

data class Check(
    val id: Int,
    val idUser: String,
    val idPuntoventa: String,
    val fechaRegistro: String,
    val latitud: String,
    val longitud: String,
    val comment: String,
){
    fun toModel() = CheckModel(id,idUser,idPuntoventa,fechaRegistro,latitud,longitud,comment)
}