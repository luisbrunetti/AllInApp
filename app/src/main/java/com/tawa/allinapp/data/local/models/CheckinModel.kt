package com.tawa.allinapp.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Checkin
import com.tawa.allinapp.models.PV

@Entity(tableName = "checkin")
data class CheckinModel (
    @PrimaryKey (autoGenerate = true)  val id: Int,
    val idUser: String,
    val idPuntoventa: String,
    val fechaRegistro: String,
    val latitud: String,
    val longitud: String,
    val comment: String,
){
    fun toView() = Checkin(id,idUser,idPuntoventa,fechaRegistro,latitud,longitud,comment)
}