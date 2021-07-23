package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Schedule

@Entity(tableName = "schedule")
data class ScheduleModel (
    @PrimaryKey val id: String,
    val description: String?,
    val zone: String?,
    val codGeo: String?,
    val idCompany: String?,
    val latitude: Double?,
    val longitude: Double?,
){
    fun toView() = Schedule(id,description?:"",zone?:"",codGeo?:"",idCompany?:"",latitude?:0.0,longitude?:0.0)
}