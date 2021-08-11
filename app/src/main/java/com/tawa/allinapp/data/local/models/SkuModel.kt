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
    val state : String,
    val type:String
){
    fun toView() = Sku(id,idPv,idCompany,state,type)
}