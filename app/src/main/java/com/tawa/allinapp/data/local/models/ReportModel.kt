package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Report

@Entity(tableName = "reports")
data class ReportModel(
    @PrimaryKey val id: String,
    val report: String,
    val idCompany: String,
    val idUser: String,
    val idUserMod: String,
    val feMod: String,
    val feCreate: String,
){
    fun toView() = Report(id,report,idCompany,idUser,idUserMod,feMod,feCreate)
}