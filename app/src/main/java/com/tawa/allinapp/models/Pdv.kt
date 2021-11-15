package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.PdvRemote

data class Pdv(
    val id:String,
    val idUser:String?,
    val image: String?,
    val ruc: String?,
    val pdvDescription: String?,
    val nameUser: String?,
    val phoneUser: String?,
    val latitude: Double?,
    val longitude: Double?,
    val state:String,
)

