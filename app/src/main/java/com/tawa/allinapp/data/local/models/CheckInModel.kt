package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.CheckIn

@Entity(tableName = "checkin")
data class CheckInModel (
    @PrimaryKey (autoGenerate = true)  val id: Int,
    val idUser: String,
    val idPV: String,
    val registerDate: String,
    val latitude: String,
    val longitude: String,
    val comment: String,
){
    fun toView() = CheckIn(id,idUser,idPV,registerDate,latitude,longitude,comment)
}