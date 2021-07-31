package com.tawa.allinapp.models


import com.tawa.allinapp.data.local.models.SkuModel
import com.tawa.allinapp.data.local.models.SkuObservationModel

data class SkuObservation(
    val id:Int ,
    val idSkuDetail :String,
    val observation:String
){
    fun toModel() = SkuObservationModel(id,idSkuDetail,observation)
}