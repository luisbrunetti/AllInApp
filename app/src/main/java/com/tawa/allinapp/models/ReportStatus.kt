package com.tawa.allinapp.models

import com.google.gson.annotations.SerializedName

data class ReportStatus(
    val status: String,
    val lastConnection: String,
    val lastLatitude: Double,
    val lastLongitude: Double,
    val battery: Int,
    val name:String,
)