package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tawa.allinapp.models.Pdv

@Entity(tableName = "pdv")
data class PdvModel(
    @PrimaryKey val id:String,
    val image: String?,
    val ruc: String?,
    val pdvDescription: String?,
    val nameUser: String?,
    val phoneUser: String?,
    val latitude: Double?,
    val longitude: Double?,
    val state : String,
){
    fun toView() = Pdv(id,image,ruc,pdvDescription,nameUser,phoneUser,latitude,longitude,state)
}