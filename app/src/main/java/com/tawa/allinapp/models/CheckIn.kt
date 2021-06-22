package com.tawa.allinapp.models

import com.tawa.allinapp.data.local.models.CheckInModel

data class CheckIn(
    val id: Int,
    val idUser: String,
    val idPV: String,
    val registerDate: String,
    val latitude: String,
    val longitude: String,
    val comment: String,
){
    fun toModel() = CheckInModel(id,idUser,idPV,registerDate,latitude,longitude,comment)
}