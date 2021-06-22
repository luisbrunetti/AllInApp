package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Check

@Entity(tableName = "check")
data class CheckModel (
    @PrimaryKey (autoGenerate = true) val id: Int,
    val idUser: String,
    val idPV: String,
    val registerDate: String,
    val latitude: String,
    val longitude: String,
    val comment: String,
){
    fun toView() = Check(id,idUser,idPV,registerDate,latitude,longitude,comment)
}