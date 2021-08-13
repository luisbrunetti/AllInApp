package com.tawa.allinapp.models

import com.tawa.allinapp.features.init.usecase.SetIdPv

data class Report(
    val id: String,
    val reportName: String,
    val idCompany: String,
    val nameCompany:String,
    val idUser: String,
    val idUserMod: String,
    val feMod: String,
    val feCreate: String,
    val state:String = "No Iniciado",
    val type:String,
    val idPv: String

)