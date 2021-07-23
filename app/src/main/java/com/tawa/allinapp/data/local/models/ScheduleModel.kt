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
    val latitude: String?,
    val longitude: String?,
){
    fun toView() = Schedule(id,description?:"",zone?:"",codGeo?:"",idCompany?:"",latitude?:"",longitude?:"")
}