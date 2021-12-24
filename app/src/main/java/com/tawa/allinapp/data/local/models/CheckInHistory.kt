package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import com.tawa.allinapp.models.CheckInHistoryView

@Entity(tableName = "CheckInHistory", primaryKeys = ["idUser", "idPv", "idCompany"])
data class CheckInHistory(
    val idUser: String,
    val idPv: String,
    val idCompany: String,
    val pvName:String,
    val pending: String
){
    fun toView() = CheckInHistoryView(idUser = idUser, idPv = idPv, pvName=pvName, idCompany = idCompany, pending = pending)
}