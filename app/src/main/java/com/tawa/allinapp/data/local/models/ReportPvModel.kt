package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.ReportPv

@Entity(tableName = "report_pv")
data class ReportPvModel(
    @PrimaryKey (autoGenerate = true)  val id: Int,
    val idPv: String,
    val idReport: String,
    val idUser:String,
    val state: String,
    val type:String,
    val time :String,
    val latitude:String,
    val longitude:String
){
    fun toView() = ReportPv(id,idPv,idReport,idUser,state,type,time,latitude,longitude)
}