package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.PV

@Entity(tableName = "pv")
data class PVModel (
    @PrimaryKey val id: String,
    val description: String?,
    val zone: String?,
    val codGeo: String?,
    val idCompany: String?,
    val latitude: String?,
    val longitude: String?,
){
    fun toView() = PV(id,description?:"",zone?:"",codGeo?:"",idCompany?:"",latitude?:"",longitude?:"")
}