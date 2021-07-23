package com.tawa.allinapp.models

data class Schedule(
    val id: String,
    val description: String,
    val zone: String,
    val codGeo: String,
    val idCompany: String,
    val lat: String,
    val long: String,
)