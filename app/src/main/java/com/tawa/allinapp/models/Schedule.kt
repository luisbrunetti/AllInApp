package com.tawa.allinapp.models

data class Schedule(
    val id: String,
    val pv:String,
    val description: String,
    val zone: String,
    val codGeo: String,
    val idCompany: String,
    val lat: Double,
    val long: Double,
    val idUser:String
)