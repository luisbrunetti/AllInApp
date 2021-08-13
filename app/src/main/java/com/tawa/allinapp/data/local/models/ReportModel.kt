package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Report

@Entity(tableName = "reports")
data class ReportModel(
    @PrimaryKey val id: String,
    val reportName: String,
    val idCompany: String,
    val nameCompany:String,
    val idUser: String,
    val idUserMod: String,
    val feMod: String,
    val feCreate: String,
    val state:String,
    val type:String,
    val idPv:String
){
    fun toView() = Report(id,reportName,idCompany,nameCompany,idUser,idUserMod,feMod,feCreate,state,type,idPv)
}