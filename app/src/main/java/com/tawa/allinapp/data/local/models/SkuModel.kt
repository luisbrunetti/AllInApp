package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Report
import com.tawa.allinapp.models.Sku

@Entity(tableName = "sku")
data class SkuModel(
    @PrimaryKey val id: String,
    val idPv: String,
    val idCompany: String,
    val idUser:String,
    val state : String,
    val type:String,
    val dateCreation:String,
    val latitude:String,
    val longitude:String
){
    fun toView() = Sku(id,idPv,idCompany,idUser,state,type,dateCreation,latitude,longitude)
}