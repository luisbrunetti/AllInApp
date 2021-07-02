package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.features.init.usecase.SetIdCompany
import com.tawa.allinapp.models.Company
import com.tawa.allinapp.models.Report

@Entity(tableName = "reports")
data class ReportModel(
    @PrimaryKey val id: String,
    val report: String,
    val idCompany: String,
    val idUser: String,
){
    fun toView() = Report(id,report,idCompany,idUser)
}