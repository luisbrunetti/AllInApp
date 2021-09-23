package com.tawa.allinapp.models

data class PhotoReport (
    var before:List<String>,
    var after:List<String>,
    var comments:String,
    val createAt:String,
    val longitude: Double?,
    val latitude: Double?,
)