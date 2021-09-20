package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Schedule

@Entity(tableName = "schedule",primaryKeys = ["pv","idUser"])
data class ScheduleModel (
    val id: String,
    val pv:String,
    val description: String?,
    val zone: String?,
    val codGeo: String?,
    val idCompany: String?,
    val latitude: Double?,
    val longitude: Double?,
    val idUser:String,
    val nameCorp : String,
){
    fun toView() = Schedule(id, pv,description?:"",zone?:"",codGeo?:"",idCompany?:"",latitude?:0.0,longitude?:0.0,idUser,nameCorp)
}