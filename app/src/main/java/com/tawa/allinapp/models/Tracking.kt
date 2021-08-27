package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.RoutesRemote


data class Tracking(
    val id: String,
    val Pv: String,
    val zoneName:String,
    val zoneDistrict:String,
    val dirCorpPv: String,
    val nameCorpPv: String,
    val latitude: Double?,
    val longitude: Double?,
    val visits: List<RoutesRemote.Visits?>,
    val tasks: List<RoutesRemote.Tasks?>
)