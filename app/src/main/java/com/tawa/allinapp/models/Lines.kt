package com.tawa.allinapp.models

import com.tawa.allinapp.data.remote.entities.ReportsSkuRemote

data class Lines(
    val id:String ,
    val stock :Boolean,
    val exhibition:Boolean,
    val price :Float,
    val observation:ArrayList<String>,
){
    fun toRequest() = ReportsSkuRemote.LineasRespuesta(id,stock,exhibition,price,observation)
}