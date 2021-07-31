package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.Sku
import com.tawa.allinapp.models.SkuObservation

@Entity(tableName = "sku_observation")
data class SkuObservationModel(
    @PrimaryKey(autoGenerate = true) val id:Int ,
    val idSkuDetail :String,
    val observation:String
){
    fun toView() = SkuObservation(id,idSkuDetail,observation)
}