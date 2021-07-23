package com.tawa.allinapp.models

import com.google.gson.annotations.SerializedName

data class ReportStatus(
    val status: String? =null,
    val lastConnection: String? =null,
    val lastLatitude: Double? =null,
    val lastLongitude: Double? =null,
    val battery: Int? =null,
    val name:String? =null,
)