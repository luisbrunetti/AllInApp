package com.tawa.allinapp.models

data class Checkin(
    val id: Int,
    val idUser: String,
    val idPuntoventa: String,
    val fechaRegistro: String,
    val latitud: String,
    val longitud: String,
    val comment: String,
)