package com.tawa.allinapp.models

data class Report(
    val id: String,
    val report: String,
    val idCompany: String,
    val idUser: String,
    val idUserMod: String,
    val feMod: String,
    val feCreate: String,
)