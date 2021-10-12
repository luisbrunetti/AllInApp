package com.tawa.allinapp.data.local.models

import androidx.room.Entity

@Entity(tableName = "Tasks", primaryKeys = ["id","idPv"])
data class TaskModel(
    val id:String,
    val idPv:String,
    val taskName:String,
    val taskState:String,
    val idUser:String,
    val feMod:String,
    val feCreate:String,
    val type:String
)