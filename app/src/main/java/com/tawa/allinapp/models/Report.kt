package com.tawa.allinapp.models

data class Report(
    val id: String,
    val reportName: String,
    val idCompany: String,
    val idUser: String,
    val idUserMod: String,
    val feMod: String,
    val feCreate: String,
)