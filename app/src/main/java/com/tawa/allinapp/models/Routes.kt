package com.tawa.allinapp.models



data class Routes(
    val id: String?,
    val Pv: String?,
    val zoneName:String,
    val zoneDistrict:String,
    val dirCorpPv: String,
    val nameCorpPv: String?,
    val latitude: Double?,
    val longitude: Double?,
    val visitTotal :String,
    val visitComplete:String,
    val visitPending :String,
    val taskTotal :String,
    val taskComplete:String,
    val taskPending :String

)