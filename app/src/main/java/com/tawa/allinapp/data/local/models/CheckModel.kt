package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Check

@Entity(tableName = "check")
data class CheckModel (
    @PrimaryKey (autoGenerate = true) val id: Int,
    val schedule: String,
    val company: String,
    val pv: String,
    val idUser: String,
    val registerDate: String,
    val latitude: String,
    val longitude: String,
    val comment: String,
){
    fun toView() = Check(id,schedule,company,pv,idUser,registerDate,latitude,longitude,comment)
}