package com.tawa.allinapp.models

data class TrackingInform(
    val idUser: String,
    val nameUser: String,
    val listTracking: List<Tracking>
)