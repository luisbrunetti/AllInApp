package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.RoutesRemote
import com.google.gson.annotations.SerializedName



data class Tracking(
    val id: String,
    val Pv: String,
    val zoneName:String,
    val zoneDistrict:String,
    val dirCorpPv: String,
    val nameCorpPv: String,
    val latitude: Double?,
    val longitude: Double?,
    val codPvCop: String,
    val visits: List<RoutesRemote.Visits?>,
    val tasks: List<RoutesRemote.Tasks?>,
    val checks: RoutesRemote.Checks,
    val reports: RoutesRemote.Reports

)


