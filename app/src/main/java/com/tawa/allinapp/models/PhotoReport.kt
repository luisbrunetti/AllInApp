package com.tawa.allinapp.models

data class PhotoReport (
    val before:List<String?>,
    val after:List<String?>,
    val comments:String,
    val createAt:String
)