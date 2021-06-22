package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.CheckinModel

data class Checkin(
    val id: Int,
    val idUser: String,
    val idPuntoventa: String,
    val fechaRegistro: String,
    val latitud: String,
    val longitud: String,
    val comment: String,
){
    fun toModel() = CheckinModel(id,idUser,idPuntoventa,fechaRegistro,latitud,longitud,comment)
}