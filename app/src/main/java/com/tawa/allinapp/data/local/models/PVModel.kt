package com.tawa.allinapp.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pv")
data class PVModel (
    @PrimaryKey val id: String,
    val description: String,
    val zone: String,
    val codGeo: String,
    val idCompany: String,
)