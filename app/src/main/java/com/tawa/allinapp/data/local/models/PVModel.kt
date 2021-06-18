package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.features.auth.PV

@Entity(tableName = "pv")
data class PVModel (
    @PrimaryKey val id: String,
    val description: String,
    val zone: String,
    val codGeo: String,
    val idCompany: String,
){
    fun toView() = PV(id,description,zone,codGeo,idCompany)
}